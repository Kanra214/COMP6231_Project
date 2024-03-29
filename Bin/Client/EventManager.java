package Client;


public class EventManager extends Client{

    public EventManager(String[] information) {
        super(information);
    }

    public String addEvent(String ID, String eventId, String eventType, int bookingCapacity) {
        String[] params = new String[3];
        params[1] = eventId;
        params[2] = eventType + "\n" + "          bookingCapacity: " + Integer.toString(bookingCapacity) + "\n";
        this.log.requestInfo("addEvent", params);
        String status = null;
        status = this.service.addEvent(ID, eventId,eventType,bookingCapacity);

        this.log.info("addEvent",params,status);
        return status;
    }

    public String removeEvent(String ID, String eventID, String eventType) {
        String[] params = new String[3];
        params[1] = eventID;
        params[2] = eventType;
        log.requestInfo("removeEvent", params);
        String status = null;
        status = this.service.removeEvent(ID, eventID,eventType);
//        System.out.println(status);
        log.info("removeEvent", params, status);
        return status;
    }

    public String listEventAvailability (String ID, String eventType) {
        String[] params = new String[3];
        params[2] = eventType;
        log.requestInfo("listEventAvailability",params);
        String status = null;
        status = this.service.listEventAvailability(ID, eventType);

        log.info("listEventAvailability",params,status);
//        System.out.println(status);
        return status;
    }

 }
