package Test.src;

import Common.JsonObject;
import RMs.src.RMID;

import java.io.IOException;
import java.net.*;

import static Common.Requests.*;

public class Test{
    private static DatagramSocket sendSocket;
    private static DatagramSocket receiveSocket;
    private static int sendPort = 9999;
    private static int receivePort = 9998;
    public static void main(String[] args) throws SocketException, InterruptedException {
        sendSocket = new DatagramSocket(sendPort);
        receiveSocket = new DatagramSocket(receivePort);
        new Thread(new ListenThread()).start();
        int i = 1;


        //1
        System.out.println("-------------------test" + i++ + "-------------------");
        JsonObject o = new JsonObject();
        o.setClientId("TORM3456");
        o.setRequest(AddEvent);
        o.setEventId("TORE080619");
        o.setEventType("Conference");
        o.setCapacity(2);
        o.setSeqNum(0);
        send(o);
        o.setEventId("TORE110619");
        o.setEventType("Seminar");
        o.setCapacity(1);
        o.setSeqNum(1);
        send(o);


        //2
        System.out.println("-------------------test" + i++ + "-------------------");
        o.setClientId("MTLM9000");
        o.setRequest(AddEvent);
        o.setEventId("MTLA090619");
        o.setEventType("Conference");
        o.setCapacity(2);
        o.setSeqNum(2);
        send(o);
        o.setEventId("MTLA080619");
        o.setEventType("TradeShow");
        o.setCapacity(1);
        o.setSeqNum(3);
        send(o);

        //3
        System.out.println("-------------------test" + i++ + "-------------------");
        o.setClientId("OTWM9000");
        o.setRequest(AddEvent);
        o.setEventId("OTWA190619");
        o.setEventType("Conference");
        o.setCapacity(1);
        o.setSeqNum(4);
        send(o);
        o.setEventId("OTWA250619");
        o.setEventType("Seminar");
        o.setCapacity(1);
        o.setSeqNum(5);
        send(o);
        //4
        System.out.println("-------------------test" + i++ + "-------------------");
        o.setRequest(AddEvent);
        o.setEventId("OTWA190619");
        o.setEventType("Conference");
        o.setCapacity(1);
        o.setSeqNum(6);
        send(o);
        o = new JsonObject();
        o.setRequest(ListEventAvailability);
        o.setClientId("OTWM9000");
        o.setEventType("Conference");
        o.setSeqNum(7);
        send(o);
        o.setEventType("Seminar");
        o.setSeqNum(8);
        send(o);
        o.setEventType("TradeShow");
        o.setSeqNum(9);
        send(o);


        //5
        System.out.println("-------------------test" + i++ + "-------------------");
        o = new JsonObject();
        o.setClientId("OTWC1234");
        o.setRequest(BookEvent);
        o.setEventId("TORE080619");
        o.setEventType("Conference");
        o.setSeqNum(10);
        send(o);

        o.setEventId("TORE110619");
        o.setEventType("Seminar");
        o.setSeqNum(11);
        send(o);

        o.setEventId("MTLA090619");
        o.setEventType("Conference");
        o.setSeqNum(12);
        send(o);

        o.setEventId("OTWA190619");
        o.setEventType("Conference");
        o.setSeqNum(13);
        send(o);
        o = new JsonObject();
        o.setClientId("OTWC1234");
        o.setRequest(GetBookingSchedule);
        o.setSeqNum(14);
        send(o);

        //6
        System.out.println("-------------------test" + i++ + "-------------------");
        o = new JsonObject();
        o.setRequest(SwapEvent);
        o.setClientId("OTWC1234");
        o.setEventId("MTLA080619");
        o.setEventType("TradeShow");
        o.setOldEventId("OTWA190619");
        o.setOldEventType("Conference");
        o.setSeqNum(15);
        send(o);

        o.setEventId("OTWA250619");
        o.setEventType("Seminar");
        o.setOldEventId("TORE080619");
        o.setOldEventType("Conference");
        o.setSeqNum(16);
        send(o);

        o = new JsonObject();
        o.setClientId("OTWC1234");
        o.setRequest(GetBookingSchedule);
        o.setSeqNum(17);
        send(o);

        //7
        System.out.println("-------------------test" + i++ + "-------------------");
        o = new JsonObject();
        o.setClientId("TORC1234");
        o.setManagerId("TORM3456");
        o.setRequest(BookEvent);
        o.setEventId("MTLA080619");
        o.setEventType("TradeShow");
        o.setSeqNum(18);
        send(o);

        o.setRequest(SwapEvent);
        o.setEventId("OTWA250619");
        o.setEventType("Seminar");
        o.setOldEventId("TORE080619");
        o.setOldEventType("Conference");
        o.setSeqNum(19);
        send(o);

        o.setEventId("TORE080619");
        o.setEventType("Seminar");
        o.setOldEventId("MTLA090619");
        o.setOldEventType("Conference");
        o.setSeqNum(20);
        send(o);

        o.setRequest(GetBookingSchedule);
        o.setSeqNum(21);
        send(o);


        //8
        System.out.println("-------------------test" + i++ + "-------------------");
        o = new JsonObject();
        o.setClientId("MTLM9000");
        o.setRequest(ListEventAvailability);
        o.setEventType("Conference");
        o.setSeqNum(22);
        send(o);

        o.setEventType("Seminar");
        o.setSeqNum(23);
        send(o);

        o.setEventType("TradeShow");
        o.setSeqNum(24);
        send(o);

        //9
        System.out.println("-------------------test" + i++ + "-------------------");
        o = new JsonObject();
        o.setRequest(FixServer);
        o.setSeqNum(25);
        send(o);

        //10
        System.out.println("-------------------test" + i++ + "-------------------");
        o.setClientId("MTLM9000");
        o.setRequest(AddEvent);
        o.setEventId("MTLA090719");
        o.setEventType("Conference");
        o.setCapacity(2);
        o.setSeqNum(26);
        send(o);
        //11
        System.out.println("-------------------test" + i++ + "-------------------");
        o = new JsonObject();
        o.setRequest(RestartServer);
        o.setSeqNum(27);
        send(o);





//        //9
//        System.out.println("-------------------test" + i++ + "-------------------");
//        Handler torHandler = getRelatedHandler("TORC1234");
//        Handler otwHandler = getRelatedHandler("OTWC1234");
//        Runnable r1 = new Task(torHandler, "TORC1234", "OTWA190619", "Conference", "MTLA080619", "TradeShow", 1);
//        Runnable r2 = new Task(otwHandler, "OTWC1234", "MTLA080619", "TradeShow", "OTWA190619", "Conference", 2);
//        Runnable r3 = new Task(torHandler, "TORC1234", "MTLA090619", "Conference", "MTLA080619", "TradeShow", 3);
//        Runnable r4 = new Task(otwHandler, "OTWC1234", "MTLA080619", "TradeShow", "OTWA190619", "Conference", 4);
//
//        Thread t1 = new Thread(r1);
//        Thread t2 = new Thread(r2);
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
//        Thread t3 = new Thread(r3);
//        Thread t4 = new Thread(r4);
//        t3.start();
//        t4.start();
//        t3.join();
//        t4.join();
//        tempId = "MTLM9000";
//        tempHandler = getRelatedHandler(tempId);
//        getResp(tempHandler.listEventAvailability(tempId, "Conference"));
//        getResp(tempHandler.listEventAvailability(tempId, "Seminar"));
//        getResp(tempHandler.listEventAvailability(tempId, "TradeShow"));
    }
    private static void send(JsonObject o){
        send(o, true, null);
    }
    private static void send(JsonObject o, boolean toAll, RMID toRmid){
        try {
            o.setSourcePort(receivePort);
            o.setSourceIp("127.0.0.1");
            byte[] msgBytes = (o.objectToString()).getBytes();
            InetAddress host = InetAddress.getByName("localhost");
            if(toAll) {
                for (RMID rmid : RMID.values()) {
                    DatagramPacket ackDp = new DatagramPacket(msgBytes, msgBytes.length, host, rmid.getListeningPort());
                    sendSocket.send(ackDp);
                }
            }
            else{
                DatagramPacket ackDp = new DatagramPacket(msgBytes, msgBytes.length, host, toRmid.getListeningPort());
                sendSocket.send(ackDp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    static class ListenThread implements Runnable{

        @Override
        public void run() {
            while(true) {
                System.out.println("listening");
                byte[] buffer = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                try {
                    receiveSocket.receive(dp);
                    String msg = new String(dp.getData(), 0, dp.getLength());
                    JsonObject reply = JsonObject.stringToObject(msg);
                    System.out.println("###########" + reply.getSeqNum() + "###########: " + reply.getResponse());
                    byte[] ackMsgBytes = ("ACK" + reply.getSeqNum()).getBytes();
                    InetAddress dest = dp.getAddress();
                    DatagramPacket ackDp = new DatagramPacket(ackMsgBytes,ackMsgBytes.length, dest, dp.getPort());
                    receiveSocket.send(ackDp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
