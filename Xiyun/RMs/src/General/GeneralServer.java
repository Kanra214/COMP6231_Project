package RMs.src.General;

import Common.JsonObject;

public interface GeneralServer {
    void start();
    void setBug();
    void fixBug();
    JsonObject addEvent(String clientId, String eventId, String eventType, int capacity);
    JsonObject removeEvent(String clientId, String eventId, String eventType);
    JsonObject listEventAvailability(String clientId, String eventType);
    JsonObject bookEvent(String clientId, String managerId, String eventId, String eventType);
    JsonObject cancelEvent(String clientId, String managerId, String eventId, String eventType);
    JsonObject getBookingSchedule(String clientId, String managerId);
    JsonObject swapEvent(String clientId, String managerId, String newEventId, String newEventType, String oldEventId, String oldEventType);
}
