package Server;

import General.GeneralServer;
import common.Log;
import Common.JsonObject;


public class Server implements GeneralServer {

    private int port;
    private String branch;
    private Service service;
    private EventService eventService;
    private Log log;

    public Server (int port, String branch) {
        this.port = port;
        this.branch = branch;
        this.log = new Log(branch + "_server");
        this.eventService = new EventService(branch);
        this.service = new Service(this.eventService,this.log);

    }
    public void startThread() {
        Thread udpThread = new Thread(() -> this.startUDP());
        udpThread.start();
    }

    private void startUDP() {
        UDPHandler udp = new UDPHandler(this.port,this.eventService,this.log);
        udp.listen();
    }

    @Override
    public void start() {
        startThread();
    }

    @Override
    public void setBug() {//TODO

    }

    @Override
    public void fixBug() {//TODO

    }

    @Override
    public JsonObject addEvent(String clientId, String eventId, String eventType,
        int capacity) {

        JsonObject o = new JsonObject();
        String status = this.service.addEvent(eventId,eventType,capacity);
        if (status.equals("Success")) {
            o.setApproved(true);
        }else {
            o.setApproved(false);
        }
        o.setResponse(status);
        return o;
    }

    @Override
    public JsonObject removeEvent(String clientId, String eventId, String eventType) {
        JsonObject o = new JsonObject();
        String status = this.service.removeEvent(eventId,eventType);
        if (status.equals("Success")) {
            o.setApproved(true);
        }else {
            o.setApproved(false);
        }
        o.setResponse(status);
        return o;
    }

    @Override
    public JsonObject listEventAvailability(String clientId, String eventType) {
        JsonObject o = new JsonObject();
        String status = this.service.listEventAvailability(eventType);
        o.setApproved(true);
        o.setResponse(status);
        return o;
    }

    @Override
    public JsonObject bookEvent(String clientId, String managerId, String eventId,
        String eventType) {
        JsonObject o = new JsonObject();
        String status = this.service.bookEvent(clientId,eventId,eventType);
        if (status.equals("Success")) {
            o.setApproved(true);
        }else {
            o.setApproved(false);
        }
        o.setResponse(status);
        return o;
    }

    @Override
    public JsonObject cancelEvent(String clientId, String managerId, String eventId,
        String eventType) {
        JsonObject o = new JsonObject();
        String status = this.service.cancelEvent(clientId,eventId,eventType);
        if (status.equals("Success")) {
            o.setApproved(true);
        }else {
            o.setApproved(false);
        }
        o.setResponse(status);
        return o;
    }

    @Override
    public JsonObject getBookingSchedule(String clientId, String managerId) {
        JsonObject o = new JsonObject();
        String status = this.service.getBookingSchedule(clientId);
        o.setApproved(true);
        o.setResponse(status);
        return o;
    }

    @Override
    public JsonObject swapEvent(String clientId, String managerId, String newEventId,
        String newEventType, String oldEventId, String oldEventType) {
        JsonObject o = new JsonObject();
        String status = this.service.swapEvent(clientId,newEventId,newEventType,oldEventId,oldEventType);
        if (status.equals("Success")) {
            o.setApproved(true);
        }else {
            o.setApproved(false);
        }
        o.setResponse(status);
        return o;
    }
}
