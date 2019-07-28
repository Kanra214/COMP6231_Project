package RMs.src;

import Common.JsonObject;
import Common.Requests;
import RMs.src.RMID;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class ReplicaManager {
    private RMID rmid;
    private PriorityQueue<JsonObject> deliverQueue;
    private BlockingQueue<JsonObject> taskQueue,backupQueue,replyQueue;

    private CenterServer cs;
    private static final int INIT_SEQ = 0;
    private int expectingSeq;
    private DatagramSocket listeningSocket, replyingSocket, RMSocket;

    public ReplicaManager(RMID rmid) throws SocketException {
        this.rmid = rmid;
        Comparator<JsonObject> comp = new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject o1, JsonObject o2) {
                return o1.getSeqNum() - o2.getSeqNum();
            }
        };
        this.deliverQueue = new PriorityQueue<>(comp);
        this.taskQueue = new LinkedBlockingDeque<>();
        this.backupQueue = new LinkedBlockingDeque<>();
        this.replyQueue = new LinkedBlockingDeque<>();
        this.listeningSocket = new DatagramSocket(this.rmid.getListeningPort());
        this.replyingSocket = new DatagramSocket(this.rmid.getReplyingPort());
        this.RMSocket = new DatagramSocket(this.rmid.getBetweenRM());

    }

    public void start() {
        cs = new CenterServer(this.rmid);//TODO: hard code hasBug. Only XiyunServer will have bug
        cs.setTaskQueue(this.taskQueue);
        cs.setReplyQueue(this.replyQueue);
        this.expectingSeq = INIT_SEQ;
        ReplyToFE rtfe = new ReplyToFE(this.replyQueue);
        Thread service = new Thread(cs);
        Thread toFE = new Thread(rtfe);
//        Thread killer = new Thread(new KillReplica(service));
//        killer.start();
        service.start();
        toFE.start();
        byte[] buffer = new byte[1024];
        while (true) {//dont block the thread inside the loop except for receive()
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            try {
                listeningSocket.setSoTimeout(300);
                listeningSocket.receive(request);
                String msg = new String(request.getData(), 0, request.getLength());
                System.out.println("receive " + msg);
                JsonObject o = JsonObject.stringToObject(msg);

                //TODO:ack back, need to agree on port to send ack



                   String ackMsg = "ACK" + o.getSeqNum();//TODO:need agreement on this message
                    if (o.getSeqNum() >= this.expectingSeq && !this.deliverQueue.contains(o.getSeqNum())) {
                        this.deliverQueue.add(o);
                        System.out.println("Put into deliverQueue " + o.getSeqNum());
                        while (!this.deliverQueue.isEmpty() && this.deliverQueue.peek().getSeqNum() == this.expectingSeq) {
                            JsonObject task = this.deliverQueue.poll();
                            if(task.getRequest() == Requests.RestartServer){
                                restartServer();
                            }
                            else if (task.getRequest() == Requests.FixServer){
                                cs.fixBug();

                            }
                            else {
                                this.taskQueue.add(task);
                                this.backupQueue.add(task);

                            }
                            this.expectingSeq++;
                            System.out.println("Expecting " + this.expectingSeq);
                        }
                    } else {
                        System.out.println("Discard " + o.getSeqNum());
                    }


                ack(ackMsg, request.getAddress(), request.getPort());


            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void ack(String ackMsg, InetAddress dest, int port) {
        byte[] bytes = ackMsg.getBytes();
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, dest, port);
        try {
            replyingSocket.send(dp);
            System.out.println("--------ACK back " + ackMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restartServer() {
        System.out.println("############restarting server");
        cs = new CenterServer(this.rmid);
        taskQueue = new LinkedBlockingDeque<>(backupQueue);
        replyQueue.clear();
        cs.setTaskQueue(taskQueue);
        cs.setReplyQueue(replyQueue);
        Thread service = new Thread(cs);
//        Thread kill = new Thread(new KillReplica(service));
//        kill.start();
        service.start();


    }

    class ReplyToFE implements Runnable {
        BlockingQueue<JsonObject> q;
        Set<Integer> replied;
        HashMap<Integer, TimerTask> pendingAckQueue;

        private ReplyToFE(BlockingQueue<JsonObject> q) {
            this.q = q;
            replied = new HashSet<>();
            pendingAckQueue = new HashMap<>();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    JsonObject reply = q.take();
                    if(reply != null) {

                        if (!replied.contains(reply.getSeqNum())) {

                            replied.add(reply.getSeqNum());
                            Timer timer = new Timer();
                            TimerTask t = new RepeatSendTask(reply);
                            timer.schedule(t,0, 2000);
                            System.out.println("timer scheduled");
                            pendingAckQueue.put(reply.getSeqNum(), t);


                        } else {
                            System.out.println("--------not sending back " + reply.getSeqNum());
                        }

                        replyingSocket.setSoTimeout(1000);
                        byte[] buffer = new byte[1024];
                        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                        replyingSocket.receive(dp);
                        String msg = new String(dp.getData(), 0, dp.getLength());
                        if (msg.substring(0, 3).equals("ACK")) {//TODO: need agreement on this message

                            int ackedSeq = Integer.parseInt(msg.substring(3));
                            pendingAckQueue.get(ackedSeq).cancel();
                            System.out.println(ackedSeq + " timer is canceled");
                            pendingAckQueue.remove(ackedSeq);

                        } else {
                            System.out.println("replyingSocket not receiving ack");
                        }
                    }
                } catch (SocketTimeoutException e) {
                    continue;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }



    class RepeatSendTask extends TimerTask {
        private JsonObject task;

        private RepeatSendTask(JsonObject task) {
            this.task = task;
        }

        @Override
        public void run() {
            System.out.println("inside timertask");
            byte[] bytes = task.objectToString().getBytes();
            try {
                InetAddress dest = InetAddress.getByName(task.getSourceIp());
                DatagramPacket dp = new DatagramPacket(bytes, bytes.length, dest, task.getSourcePort());
                replyingSocket.send(dp);
                System.out.println("--------sending back " + task.getSeqNum());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}






    class KillReplica implements Runnable{
        Thread sc;
        KillReplica(Thread sc){
            this.sc = sc;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(5000);//TODO:set a timer to kill the server
                System.out.println("killer thread awake");
                sc.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
