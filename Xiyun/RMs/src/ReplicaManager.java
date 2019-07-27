import Common.JsonObject;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class ReplicaManager {
    private RMID rmid;
    private PriorityBlockingQueue<JsonObject> deliverQueue;
    private BlockingQueue<JsonObject> taskQueue, backupQueue,replyQueue;
    private CenterServer cs;
    private static final int INIT_SEQ = 0;
    private int expectingSeq;
    private DatagramSocket listeningSocket, replyingSocket, RMSocket;
    public ReplicaManager(RMID rmid) throws SocketException {
        this.rmid = rmid;
        this.deliverQueue = new PriorityBlockingQueue<>();
        this.taskQueue = new LinkedBlockingDeque<>();
        this.backupQueue = new LinkedBlockingDeque<>();
        this.replyQueue = new LinkedBlockingDeque<>();
        this.listeningSocket = new DatagramSocket(this.rmid.getListeningPort());
        this.replyingSocket = new DatagramSocket(this.rmid.getReplyingPort());
        this.RMSocket = new DatagramSocket(this.rmid.getBetweenRM());

    }
    public void start(){
        cs = new CenterServer(this.rmid);//TODO: hard code hasBug. Only XiyunServer will have bug
        cs.setTaskQueue(this.taskQueue);
        cs.setReplyQueue(this.replyQueue);
        this.expectingSeq = INIT_SEQ;
        ReplyToFE rtfe = new ReplyToFE(this.replyQueue);
        Thread service = new Thread(cs);
        Thread toFE = new Thread(rtfe);
        Thread killer = new Thread(new KillReplica(service));
        killer.start();
        service.start();
        toFE.start();
        byte[] buffer = new byte[1024];
        while(true){
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            try {
                listeningSocket.receive(request);
                String msg = new String(request.getData(), 0, request.getLength());
                //TODO:ack back
                if(msg.equals("Restart")){//TODO:need agreement on this message
                    restartServer();
                }
                else if(msg.equals("Fix bug")){//TODO:need agreement on this message
                    cs.fixBug();
                }
                else {
                    JsonObject o = JsonObject.stringToObject(msg);
                    if(o.getSeqNum() >= this.expectingSeq && !this.deliverQueue.contains(o.getSeqNum())){
                        this.deliverQueue.put(o);
                        System.out.println("Put into deliverQueue " + o.getSeqNum());
                        while(!this.deliverQueue.isEmpty() && this.deliverQueue.peek().getSeqNum() == this.expectingSeq){
                            JsonObject task = this.deliverQueue.poll();
                            this.taskQueue.put(task);
                            this.backupQueue.put(task);
                            this.expectingSeq++;
                            System.out.println("Expecting " + this.expectingSeq);
                        }
                    }
                    else{
                        System.out.println("Discard " + o.getSeqNum());
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
    private void restartServer(){
        System.out.println("############restarting server");
        cs = new CenterServer(false);
        taskQueue = new LinkedBlockingDeque<>(backupQueue);
        replyQueue.clear();
        cs.setTaskQueue(taskQueue);
        cs.setReplyQueue(replyQueue);
        Thread service = new Thread(cs);
//        Thread kill = new Thread(new KillReplica(service));
//        kill.start();
        service.start();



    }
    class ReplyToFE implements Runnable{
        BlockingQueue<JsonObject> q;
        Set<Integer> replied;
        private ReplyToFE(BlockingQueue<JsonObject> q){
            this.q = q;
            replied = new HashSet<>();
        }
        @Override
        public void run() {
            while(true){
                try {
                    JsonObject reply = q.take();
                    if(!replied.contains(reply.getSeqNum())){
                        System.out.println("--------sending back " + reply.getSeqNum());
                        byte[] bytes = reply.objectToString().getBytes();
                        InetAddress dest = InetAddress.getByName(reply.getSourceIp());
                        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, dest, reply.getSourcePort());
                        replyingSocket.send(dp);
                        replied.add(reply.getSeqNum());
                        //TODO:receive ack here
                    }
                    else{
                        System.out.println("--------not sending back " + reply.getSeqNum());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
