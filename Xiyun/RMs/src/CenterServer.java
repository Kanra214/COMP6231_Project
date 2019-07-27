import Common.JsonObject;
import Servers.Branch;
import Servers.Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;


public class CenterServer implements Runnable {
    private Server MTLServer, TORServer, OTWServer;
    private PriorityBlockingQueue<JsonObject> deliverQueue;
    private BlockingQueue<JsonObject> taskQueue, replyQueue;
    private boolean hasBug;
    protected CenterServer(boolean hasBug){
        this.hasBug = hasBug;
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
            MTLServer.fix();
            TORServer.fix();
            OTWServer.fix();
        }
    }

    @Override
    public void run() {
        MTLServer = new Server(Branch.MTL, hasBug);
        TORServer = new Server(Branch.TOR, hasBug);
        OTWServer = new Server(Branch.OTW, hasBug);
        while(true){
            try {
                JsonObject request = taskQueue.take();
                System.out.println("Processing " + request.getSeqNum());
                Server server = getServerByBranch(request.getClientId());
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
    private Server getServerByBranch(String id){
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
