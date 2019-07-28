package BinServer.src.Server;

import BinServer.src.common.Log;




public class Service {

    private EventService eventService;
    private Log log;
    public Service (EventService eventService, Log log){
        this.eventService = eventService;
        this.log = log;
    }

    public String addEvent(String eventID, String eventType, int bookingCapacity) {
        String[] param = new String[3];
        param[1] = eventID;
        param[2] = eventType + "\n" + "          bookingCapacity: " + Integer.toString(bookingCapacity) + "\n";
        String status = eventService.addEvent(eventID,eventType,bookingCapacity);
        System.out.println("addEvent");
        System.out.println(status);
        log.info("addEvent", param, status);
        return status;
    }

    public String removeEvent(String eventID, String eventType){
        String[] param = new String[3];
        param[1] = eventID;
        param[2] = eventType;
        String status = eventService.removeEvent(eventID,eventType);
        System.out.println("removeEvent");
        System.out.println(status);
        log.info("removeEvent", param, status);

        return status;
    }

    public String listEventAvailability(String eventType) {
        String[] param = new String[3];
        param[2] = eventType;
        String status = eventService.listEventAvailability(eventType);
        System.out.println("listEventAvailability");
        System.out.println(status);
        log.info("listEventAvailability", param, status);

        return status;
    }

    public String bookEvent(String customerID, String eventID, String eventType){
        String[] param = new String[3];
        param[0] = customerID;
        param[1] = eventID;
        param[2] = eventType;
        String status = eventService.bookEvent(customerID,eventID,eventType);
        System.out.println("bookEvent");
        System.out.println(status);
        log.info("bookEvent", param, status);

        return status;
    }

    public String getBookingSchedule(String customerID){
        String[] param = new String[3];
        param[0] = customerID;
        String status = eventService.getBookingSchedule(customerID);
        System.out.println("getBookingSchedule");
        System.out.println(status);
        log.info("getBookingSchedule", param, status);


        return status;
    }

    public String cancelEvent(String customerID, String eventID, String eventType){
        String[] param = new String[3];
        param[0] = customerID;
        param[1] = eventID;
        param[2] = eventType;
        String status = eventService.cancelEvent(customerID,eventID,eventType);
        System.out.println("cancelEvent");
        System.out.println(status);
        log.info("cancelEvent", param, status);

        return status;
    }

    public String swapEvent(String customerID, String newEventID, String newEventType,
        String oldEventID, String oldEventType) {
        String[] param = new String[5];
        param[0] = customerID;
        param[1] = newEventID;
        param[2] = newEventType;
        param[3] = oldEventID;
        param[4] = oldEventType;
        String status = eventService.swapEvent(customerID,newEventID,newEventType, oldEventID, oldEventType);
        log.info("swapEvent", param, status);

        return status;
    }



}
