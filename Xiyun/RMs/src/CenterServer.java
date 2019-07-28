package RMs.src;

import BinServer.src.Server.Server;
import BinServer.src.common.CityToPort;
import Common.JsonObject;
import RMs.src.General.GeneralServer;
import RMs.src.RMID;
import Servers.Branch;
import Servers.Branch2;
import Servers.XiyunServer;
import Servers.XiyunServer2;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;


public class CenterServer implements Runnable {
    private GeneralServer MTLServer, TORServer, OTWServer;
    private BlockingQueue<JsonObject> taskQueue, replyQueue;
    private RMID rmid;
    protected CenterServer(RMID rmid){
        this.rmid = rmid;
    }

    protected void setTaskQueue(BlockingQueue<JsonObject> taskQueue){
        this.taskQueue = taskQueue;
    }
    protected void setReplyQueue(BlockingQueue<JsonObject> replyQueue){
        this.replyQueue = replyQueue;
    }
    protected void fixBug(){
        if(Thread.currentThread().isAlive()){

            System.out.println("############fixing server in centerserver");
            MTLServer.fixBug();
            TORServer.fixBug();
            OTWServer.fixBug();
        }
    }

    @Override
    public void run() {
        if(this.rmid == RMID.BinServer){
            MTLServer = new Server(CityToPort.map.get("MTL"),"MTL");
            TORServer = new Server(CityToPort.map.get("TOR"),"TOR");
            OTWServer = new Server(CityToPort.map.get("OTW"),"OTW");
        }
        else if(this.rmid == RMID.Xiyun1){
            MTLServer = new XiyunServer(Branch.MTL);
            TORServer = new XiyunServer(Branch.TOR);
            OTWServer = new XiyunServer(Branch.OTW);
        }
        else {
            MTLServer = new XiyunServer2(Branch2.MTL);
            TORServer = new XiyunServer2(Branch2.TOR);
            OTWServer = new XiyunServer2(Branch2.OTW);
            MTLServer.setBug();//TODO:hard code this

        }
        Thread t1 = new Thread(new UDPThreadStarter(MTLServer));
        Thread t2 = new Thread(new UDPThreadStarter(TORServer));
        Thread t3 = new Thread(new UDPThreadStarter(OTWServer));
        t1.start();
        t2.start();
        t3.start();

        while(true){

//                System.out.println("poll from taskQueue");

            JsonObject request = null;
            try {
                request = taskQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if(request != null) {
                    System.out.println("Processing " + request.getSeqNum());
                    GeneralServer server = getServerByBranch(request.getClientId());
                    JsonObject response;
                    switch (request.getRequest()) {
                        case AddEvent:
                            response = server.addEvent(request.getClientId(), request.getEventId(), request.getEventType(), request.getCapacity());
                            break;
                        case RemoveEvent:
                            response = server.removeEvent(request.getClientId(), request.getEventId(), request.getEventType());
                            break;
                        case ListEventAvailability:
                            response = server.listEventAvailability(request.getClientId(), request.getEventType());
                            break;
                        case BookEvent:
                            response = server.bookEvent(request.getClientId(), request.getManagerId(), request.getEventId(), request.getEventType());
                            break;
                        case CancelEvent:
                            response = server.cancelEvent(request.getClientId(), request.getManagerId(), request.getEventId(), request.getEventType());
                            break;
                        case GetBookingSchedule:
                            response = server.getBookingSchedule(request.getClientId(), request.getManagerId());
                            break;
                        case SwapEvent:
                            response = server.swapEvent(request.getClientId(), request.getManagerId(), request.getEventId(), request.getEventType(), request.getOldEventId(), request.getOldEventType());
                            break;
                        default:
                            response = null;
                            System.out.println("no request type !!!");
                            System.exit(0);
                            break;

                    }
                    response.setSeqNum(request.getSeqNum());
                    response.setSourceIp(request.getSourceIp());
                    response.setSourcePort(request.getSourcePort());
                    replyQueue.add(response);
                    System.out.println("pushed into reply queue");
//                }
//                else{
//                    System.out.println("yielding");
//                    Thread.yield();
//                }


        }
    }
    private GeneralServer getServerByBranch(String id){
        switch(id.substring(0,3)){
            case "MTL":
                return MTLServer;
            case "TOR":
                return TORServer;
            case "OTW":
                return OTWServer;
            default:
                System.out.println("Wrong server id");
                System.exit(0);
                return null;
        }
    }
    class UDPThreadStarter implements Runnable{
        GeneralServer s;
        UDPThreadStarter(GeneralServer s){
            this.s = s;
        }
        @Override
        public void run() {
            s.start();
        }
    }
}
