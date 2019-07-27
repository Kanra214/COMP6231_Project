import Common.JsonObject;
import General.GeneralServer;
import Server.Server;
import Servers.Branch;
import Servers.XiyunServer;
import common.CityToPort;

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
        else {
            MTLServer = new XiyunServer(Branch.MTL);
            TORServer = new XiyunServer(Branch.TOR);
            OTWServer = new XiyunServer(Branch.OTW);
        }
        if(this.rmid == RMID.Xiyun2){
            MTLServer.setBug();//TODO:hard code this
        }
        while(true){
            try {
                JsonObject request = taskQueue.take();
                System.out.println("Processing " + request.getSeqNum());
                GeneralServer server = getServerByBranch(request.getClientId());
                JsonObject response;
                switch(request.getRequest()){
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
                        response = server.swapEvent(request.getClientId(), request.getManagerId(), request.getEventId(),request.getEventType(),request.getOldEventId(), request.getOldEventType());
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
                replyQueue.put(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
}
