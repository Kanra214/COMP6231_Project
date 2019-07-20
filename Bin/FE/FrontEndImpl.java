package FE;
import FrontEnd.FrontEndCorbaPOA;


public class FrontEndImpl extends FrontEndCorbaPOA{

    @Override
    public String addEvent(String eventID, String eventType, int bookingCapacity) {
        return null;
    }

    @Override
    public String removeEvent(String eventID, String eventType) {
        return null;
    }

    @Override
    public String listEventAvailability(String eventType) {
        return null;
    }

    @Override
    public String bookEvent(String customerID, String eventID, String eventType) {
        return null;
    }

    @Override
    public String getBookingSchedule(String customerID) {
        return null;
    }

    @Override
    public String cancelEvent(String customerID, String eventID, String eventType) {
        return null;
    }

    @Override
    public String swapEvent(String customerID, String newEventID, String newEventType,
        String oldEventID, String oldEventType) {
        return null;
    }
}
