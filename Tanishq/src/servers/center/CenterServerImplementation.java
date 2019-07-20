package servers.center;

import models.EventDetails;
import models.EventType;
import models.Events;
import models.Location;
import servers.distributed.Montreal;
import servers.distributed.Ottawa;
import servers.distributed.Toronto;
import servers.udp.UDPServer;
import utils.Constants;
import utils.LoggerUtility;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

public class CenterServerImplementation extends CenterServerIdl.CenterServerPOA {

    private Location location;
    private Logger logger;
    private static HashMap<Location, CenterServerInformation> centralRepository = new HashMap<Location, CenterServerInformation>();
    private UDPServer udpServer;

    public CenterServerImplementation(Location location) throws IOException {
        this.location = location;
        initLogger();
        udpServer = new UDPServer(this);
        sendEventDetailsToCentralRepository();
        udpServer.start();
        addDummyData();
    }

    private void addDummyData() {
        addEvent("OTWA100501", "Conferences", 2);
        addEvent("OTWE100502", "Conferences", 2);
        addEvent("OTWM100503", "Seminars", 2);
        addEvent("OTWM100504", "Trade Shows", 2);
        addEvent("OTWM100504", "Trade Shows", 1);
        addEvent("OTWE080619", "Conferences", 1);
        addEvent("OTWE110619", "Conferences", 1);

        addEvent("TORM100505", "Trade Shows", 2);
        addEvent("TORM100506", "Seminars", 2);
        addEvent("TORM100507", "Conferences", 2);
        addEvent("TORM100507", "Conferences", 1);

        addEvent("MTLE100508", "Conferences", 2);
        addEvent("MTLE100509", "Seminars", 2);
        addEvent("MTLE100510", "Trade Shows", 2);
        addEvent("MTLE100509", "Seminars", 1);
        addEvent("MTLE100509", "Seminars", 3);
        addEvent("MTLE100510", "Trade Shows", 1);
        addEvent("MTLA090619", "Conferences", 2);
        addEvent("MTLA080619", "Trade Shows", 2);
        /*for (EventType name: Montreal.getEvents().keySet()){
            String key = name.toString();
            List<Events> value = Montreal.getEvents().get(name);
            for (Events item : value) {
                System.out.println(key + " " + item);
            }
        }

        for (EventType name: Ottawa.getEvents().keySet()){
            String key = name.toString();
            List<Events> value = Ottawa.getEvents().get(name);
            for (Events item : value) {
                System.out.println(key + " " + item);
            }
        }

        for (EventType name: Toronto.getEvents().keySet()){
            String key = name.toString();
            List<Events> value = Toronto.getEvents().get(name);
            for (Events item : value) {
                System.out.println(key + " " + item);
            }
        }*/
    }

    private void sendEventDetailsToCentralRepository() {
        centralRepository.put(location, new CenterServerInformation(location, udpServer.getUdpPort(), udpServer.getInetAddress(), new Date()));
    }

    public Location getLocation() {
        return location;
    }

    private void initLogger() throws IOException {
        logger = LoggerUtility.getLogger(location);
    }

    @Override
    public synchronized String addEvent(String eventID, String eventType, int bookingCapacity) {
        eventType = eventType.replace(" ", "");
        Location location = getLocationFromID(eventID);
        HashMap<EventType, Events> events = getEventsByLocation(location);
        if (events != null) {
            if (events.containsKey(EventType.valueOf(eventType))) {
                Events eventsList = events.get(EventType.valueOf(eventType));
                if (eventsList.getEvents().containsKey(eventID)) {
                    EventDetails eventDetails = eventsList.getEvents().get(eventID);
                    eventDetails.setBookingCapacity(bookingCapacity);
                    return Constants.ADD_EVENT_SUCCESSFUL;
                } else {
                    EventDetails eventDetails = new EventDetails(bookingCapacity, 0, new ArrayList<>());
                    eventsList.getEvents().put(eventID, eventDetails);
                    return Constants.ADD_EVENT_SUCCESSFUL;
                }
            } else {
                EventDetails eventDetails = new EventDetails(bookingCapacity, 0, new ArrayList<>());
                HashMap<String, EventDetails> event = new HashMap<>();
                event.put(eventID, eventDetails);
                events.put(EventType.valueOf(eventType), new Events(event));
                return Constants.ADD_EVENT_FAILURE;
            }
        }
        return Constants.ADD_EVENT_FAILURE;
    }

    private synchronized HashMap<EventType, Events> getEventsByLocation(Location location) {
        if (location == Location.MTL) return Montreal.getEvents();
        else if (location == Location.OTW) return Ottawa.getEvents();
        else if (location == Location.TOR) return Toronto.getEvents();
        else return null;
    }

    private synchronized Location getLocationFromID(String eventID) {
        return Location.valueOf(eventID.substring(0, 3));
    }

    @Override
    public synchronized String removeEvent(String eventID, String eventType) {
        eventType = eventType.replace(" ", "");
        Location location = getLocationFromID(eventID);
        HashMap<EventType, Events> events = getEventsByLocation(location);
        //System.out.println(events);
        if (events != null) {
            if (events.containsKey(EventType.valueOf(eventType))) {
                //System.out.println("here1");
                Events eventsList = events.get(EventType.valueOf(eventType));
                //System.out.println(eventsList.getEvents().keySet());
                if (eventsList.getEvents().containsKey(eventID)) {
                    eventsList.getEvents().remove(eventID);
                    return Constants.REMOVE_EVENT_SUCCESSFUL;
                } else return Constants.NO_EVENT_FOUND;
            }
        }
        return Constants.NO_EVENT_FOUND;
    }

    @Override
    public synchronized String listEventAvailability(String eventType) {
        eventType = eventType.replace(" ", "");
        StringBuilder result = new StringBuilder();
        for (Location location : Location.values()) {
            HashMap<EventType, Events> allEvents = new HashMap<>(getEventsByLocation(location));
            for (EventType currentEventType : allEvents.keySet()) {
                if (currentEventType == EventType.valueOf(eventType)) {
                    Events eventsList = allEvents.get(currentEventType);
                    HashMap<String, EventDetails> events = eventsList.getEvents();
                    //System.out.println(events.keySet());
                    for (String eventId : events.keySet()) {
                        //System.out.println(eventId);
                        result.append(currentEventType)
                                .append(" - ")
                                .append(eventId).append(" ")
                                .append(events.get(eventId).getBookingCapacity()).append("\n");
                    }
                }
            }

        }
        if (result.length() > 0)
            return result.toString();
        else return Constants.LIST_EVENT_AVAILABILITY_FAILURE;
    }

    @Override
    public synchronized String bookEvent(String customerID, String eventID, String eventType) {
        eventType = eventType.replace(" ", "");
        Location location = getLocationFromID(eventID);
        HashMap<EventType, Events> events = getEventsByLocation(location);
        int bookedInOtherCities = countOtherBookingsInMonth(customerID, eventID);
        System.out.println("bookings: " + bookedInOtherCities);
        if (events != null) {
            if (bookedInOtherCities < 3) {
                if (events.containsKey(EventType.valueOf(eventType))) {
                    Events eventsList = events.get(EventType.valueOf(eventType));
                    if (eventsList.getEvents().containsKey(eventID)) {
                        EventDetails eventDetails = eventsList.getEvents().get(eventID);
                        if (eventDetails.getBookingCapacity() - eventDetails.getBookedCapacity() > 0
                                && !eventDetails.getBookedBy().contains(customerID)) {
                            eventDetails.setBookedCapacity(eventDetails.getBookedCapacity() + 1);
                            eventDetails.setBookingCapacity(eventDetails.getBookingCapacity() - 1);
                            eventDetails.getBookedBy().add(customerID);
                            return Constants.BOOK_EVENT_SUCCESSFUL;
                        } else return Constants.BOOK_EVENT_FAILURE;
                    } else return Constants.NO_EVENT_FOUND;
                } else
                    return Constants.NO_EVENT_FOUND;
            } else return Constants.MORE_THAN_3_EVENTS;
        }
        return Constants.NO_EVENT_FOUND;
    }

    private int countOtherBookingsInMonth(String customerID, String eventID) {
        int count = 0;
        String yetToBookDate = eventID.substring(6);
        for (Location location : Location.values()) {
            if (location != getLocationFromID(customerID)) {
                HashMap<EventType, Events> allEvents = new HashMap<>(getEventsByLocation(location));
                for (EventType eventType : allEvents.keySet()) {
                    Events eventsList = allEvents.get(eventType);
                    HashMap<String, EventDetails> events = eventsList.getEvents();
                    //System.out.println(events.keySet());
                    for (String eventId : events.keySet()) {
                        //System.out.println(eventId);
                        if (events.get(eventId).getBookedBy().contains(customerID)) {
                            String bookedDate = eventId.substring(6);
                            if (bookedDate.equalsIgnoreCase(yetToBookDate)) {
                                System.out.println("**********************");
                                System.out.println((yetToBookDate));
                                System.out.println((bookedDate));
                                ++count;
                                System.out.println("Count: " + count);
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private int countOtherBookingsInSameMonth(String customerID, String eventID) {
        int count = 0;
        for (Location location : Location.values()) {
            if (location != getLocationFromID(customerID)) {
                HashMap<EventType, Events> allEvents = new HashMap<>(getEventsByLocation(location));
                for (EventType eventType : allEvents.keySet()) {
                    Events eventsList = allEvents.get(eventType);
                    HashMap<String, EventDetails> events = eventsList.getEvents();
                    //System.out.println(events.keySet());
                    for (String eventId : events.keySet()) {
                        //System.out.println(eventId);
                        if (events.get(eventId).getBookedBy().contains(customerID)) {
                            String yetToBookDate = eventID.substring(6);
                            String bookedDate = eventId.substring(6);
                            DateFormat formatter = new SimpleDateFormat("MMyy");
                            try {
                                Date targetDate = formatter.parse(yetToBookDate);
                                Date currentDate = formatter.parse(bookedDate);
                                int diffInDays = (int) ((targetDate.getTime() - currentDate.getTime())
                                        / (1000 * 60 * 60 * 24));
                                System.out.println(diffInDays);
                                if (diffInDays <= 30) {
                                    ++count;
                                    System.out.println("Count: " + count);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    @Override
    public synchronized String getBookingSchedule(String customerID) {
        StringBuilder result = new StringBuilder();
        for (Location location : Location.values()) {
            HashMap<EventType, Events> allEvents = new HashMap<>(getEventsByLocation(location));
            for (EventType eventType : allEvents.keySet()) {
                Events eventsList = allEvents.get(eventType);
                HashMap<String, EventDetails> events = eventsList.getEvents();
                //System.out.println(events.keySet());
                for (String eventId : events.keySet()) {
                    //System.out.println(eventId);
                    if (events.get(eventId).getBookedBy().contains(customerID)) {
                        result.append(customerID)
                                .append(" - ")
                                .append(eventId).append("\n");
                    }
                }

            }
        }
        if (result.length() > 0)
            return result.toString();
        else return Constants.BOOK_SCHEDULE_NO_EVENTS;
    }

    @Override
    public synchronized String cancelEvent(String customerID, String eventID) {

        StringBuilder result = new StringBuilder();
        for (Location location : Location.values()) {
            HashMap<EventType, Events> allEvents = new HashMap<>(getEventsByLocation(location));
            for (EventType eventType : allEvents.keySet()) {
                Events eventsList = allEvents.get(eventType);
                HashMap<String, EventDetails> events = eventsList.getEvents();
                //System.out.println(events.keySet());
                for (String eventId : events.keySet()) {
                    if (events.get(eventId).getBookedBy().contains(customerID)) {
                        events.get(eventID).getBookedBy().remove(customerID);
                        events.get(eventID).setBookedCapacity(events.get(eventID).getBookedCapacity() - 1);
                        events.get(eventID).setBookingCapacity(events.get(eventID).getBookingCapacity() + 1);
                        return Constants.CANCEL_EVENT_SUCCESSFUL;
                    }
                }
            }
        }
        return Constants.CANCEL_EVENT_FAILURE;
    }

    @Override
    public synchronized String swapEvent(String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType) {
        boolean hasBookedTheOld = false, hasCapacityInNew = false;
        oldEventType = oldEventType.replace(" ", "");
        newEventType = newEventType.replace(" ", "");
        for (Location location : Location.values()) {
            HashMap<EventType, Events> allEvents = new HashMap<>(getEventsByLocation(location));
            for (EventType eventType : allEvents.keySet()) {
                if (eventType == EventType.valueOf(oldEventType)) {
                    Events eventsList = allEvents.get(eventType);
                    HashMap<String, EventDetails> events = eventsList.getEvents();
                    //System.out.println(events.keySet());
                    for (String eventId : events.keySet()) {
                        if (events.get(eventId).getBookedBy().contains(customerID)) {
                            hasBookedTheOld = true;
                            break;
                        }
                    }
                }
            }
        }
        if (hasBookedTheOld) {
            Location location = getLocationFromID(newEventID);
            HashMap<EventType, Events> events = getEventsByLocation(location);
            if (events != null) {
                if (events.containsKey(EventType.valueOf(newEventType))) {
                    Events eventsList = events.get(EventType.valueOf(newEventType));
                    if (eventsList.getEvents().containsKey(newEventID)) {
                        EventDetails eventDetails = eventsList.getEvents().get(newEventID);
                        if (eventDetails.getBookingCapacity() - eventDetails.getBookedCapacity() > 0) {
                            hasCapacityInNew = true;
                        }
                    }
                }
            }
        }
        if (hasBookedTheOld && hasCapacityInNew) {
            cancelEvent(customerID, oldEventID);
            bookEvent(customerID, newEventID, newEventType);
            return Constants.SWAP_EVENT_SUCCESSFUL;
        } else return Constants.SWAP_EVENT_FAILURE;
    }
}
