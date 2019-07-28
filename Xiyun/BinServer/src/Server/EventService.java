package Server;


import com.sun.net.httpserver.Authenticator.Success;
import common.CityToPort;
import common.Customer;
import common.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class EventService {
   private HashMap<String, HashMap<String, Event>> EventSchedule;//
   private HashMap<String, Customer> Customers;//key: customerID, value: Customer
   private UDPHandler udpHandler;
   private String branchName;

   public EventService(String branchName) {
       this.branchName = branchName;
       this.EventSchedule = new HashMap<>();
       this.Customers = new HashMap<>();
       this.EventSchedule.put("Conference", new HashMap<>());
       this.EventSchedule.put("Seminar", new HashMap<>());
       this.EventSchedule.put("TradeShow", new HashMap<>());
   }

  public void setUdpHandler(UDPHandler udpHandler) {
    this.udpHandler = udpHandler;
  }

  //Advisor method
  public String addEvent(String eventID, String eventType, int bookingCapacity) {
      if (!checkEventID(eventID)) {
          return "EventID format is wrong";
      }
       if (!checkEventType(eventType)){
           return "We don't have this eventType";
       }
       if (!eventID.substring(0,3).equals(branchName)) {
           return "We only can add " + branchName + "event";
       }
      if (!checkEvent(eventID,eventType)){
          this.EventSchedule.get(eventType).put(eventID, new Event(eventID,bookingCapacity));
          return "Success";
      }else {
          int previoiusCapacity = this.EventSchedule.get(eventType).get(eventID).getCapacity();
          System.out.println("previous capacity : " + previoiusCapacity);
          int newCapacity = previoiusCapacity + bookingCapacity;
          System.out.println("new capacity: " + newCapacity);
          this.EventSchedule.get(eventType).get(eventID).setCapacity(newCapacity);
          return "The course has already in the system, the capacity is updated";
      }

  }

  public String removeEvent(String eventID, String eventType) {
       if (!checkEventType(eventType)) {
           return "We don't have this eventType";
       }
      if (!eventID.substring(0,3).equals(branchName)){
          return branchName +" only can remove " + branchName + " event";
      }
       if (!checkEvent(eventID,eventType)) {
           return "The system doesn't have this event";
       }


       ArrayList<String> registedCustomer = this.EventSchedule.get(eventType).get(eventID).getRegistedCustomer();
       for (String city : CityToPort.map.keySet()) {
           for (String customerID : registedCustomer ) {
               ArrayList<String> customersIDs = new ArrayList<>();
               customersIDs.add("removeEvent");
               customersIDs.add(eventID);
               if (customerID.contains(city)) {
                   customersIDs.add(customerID);
               }
               String request = dataProcess(customersIDs.toArray(new String[customersIDs.size()]));
               this.udpHandler.send(request,city);
           }

       }
       this.EventSchedule.get(eventType).remove(eventID);

       return "Success";

  }

  public void removeCutomerEvent(String[] inform) {
       for (int i = 2; i < inform.length; i++) {
           if (Customers.containsKey(inform[i])) {
               Customers.get(inform[i]).cancelEvent(inform[1]);
           }
       }
  }

  public String listEventAvailability(String eventType) {
      if (EventSchedule.get(eventType) == null) {
          return "We dont have this eventType";
      }
       ArrayList<String> eventList = new ArrayList<>();
       for (String city :CityToPort.map.keySet()) {
           String[] inform = {"showAvailableEvents",eventType};
           eventList.add(udpHandler.send(dataProcess(inform),city));
       }
      return eventList.stream().filter(result -> result.length() != 0).collect(Collectors.joining(", "));

  }

  public String ListEventAvailabilityInLocal (String eventType) {
       HashMap<String, Event> eventMap = EventSchedule.get(eventType);
       ArrayList<Event> eventList = new ArrayList<>();
       for (Event event : eventMap.values()) {
           eventList.add(event);
       }
      return eventList.stream().map((a) -> a.getEventID() + " " + a.getAvailableSpace()).collect(Collectors.joining(", "));
  }

  public String getBookingSchedule(String customerID) {

       if (!Customers.containsKey(customerID)) {
           Customers.put(customerID,new Customer(customerID));
           return "You have not registered";
       }
       return Customers.get(customerID).getSchedule();

  }

  public String bookEvent(String customerID, String eventID, String eventType) {
       if (!checkEventType(eventType)) {
           return "We don't have this eventType";
       }
       if (!checkEvent(eventID,eventType) && checkBranch(eventID)) {
           return "The system doesn't have this event";
       }
       //if branchName is different, this operation is from manager
       if (!checkBranch(customerID)) {
           boolean a = this.EventSchedule.get(eventType).get(eventID).register(customerID);
           if (a){
               return  "Success";
           }else{
               return "Course is full";
           }
       }
      System.out.println("Customer ID: " +customerID);

       if (!Customers.containsKey(customerID)) {
           Customers.put(customerID,new Customer(customerID));
       }

       Customer customer = Customers.get(customerID);
       if (customer.checkEvent(eventID,eventType)) {
           return "The event is already registered";

       }
       if (!checkBranch(eventID)) {
           int otherCityNumber = customer.getOtherCitynum(eventID);
           if (otherCityNumber == 3) {
               return "you can't registered 3 other city events in a month";
           }
           String[] inform = {"book", customerID, eventID, eventType};
           String res = this.udpHandler.send(dataProcess(inform),eventID.substring(0,3));
           if (res.equals("Success")) {
               customer.bookEvent(eventID,eventType);
           }
           return res;
       }
       if (!checkEvent(eventID,eventType)) {
           return "We don't have this eventType";
       }
       if (!this.EventSchedule.get(eventType).get(eventID).register(customerID)) {
           return "Event is full";
       }
        customer.bookEvent(eventID,eventType);
       return "Success";

  }

  public String cancelEvent(String customerID, String eventID, String eventType) {
       Event event = this.searchEvent(eventID);
       if (!checkBranch(customerID)) {
           if (event == null) {
               return "System does not have this course";
           }
           if (event.cancel(customerID)) {
               return "Success";
           }else {
               return  "Did not register this course";
           }
       }
       if (!Customers.containsKey(customerID)){
           Customers.put(customerID,new Customer(customerID));
           return "Did not register this course";

       }
       Customer customer = Customers.get(customerID);
       if (!checkBranch(eventID)) {
           String[] inform = {"cancel",customerID, eventID, eventType};
           String res = this.udpHandler.send(dataProcess(inform), eventID.substring(0,3));
           if (res.equals("Success")) {
               customer.cancelEvent(eventID);
           }
           return res;

       }
       if (event == null) {
           return "System does not have this event";
       }
       if (event.cancel(customerID)) {
           customer.cancelEvent(eventID);
           return "Success";
       }else {
           return "Did not register this event";
       }

  }

  public Event searchEvent(String eventID) {
       Event event = null;
       for (HashMap<String, Event> tempMap : EventSchedule.values()) {
           event = tempMap.get(eventID);
           if (event != null){
               return event;
           }
       }
       return event;
  }

  public String swapEvent(String cutomerID, String newEventID, String newEventType, String oldEventID, String oldEventType) {
       Customer customer = Customers.get(cutomerID);
       if (customer == null) {
           customer = new Customer(cutomerID);
           Customers.put(cutomerID, customer );

       }
       if (!customer.checkEvent(oldEventID, oldEventType)) {
           return "We don't have the event";
       }
       if (customer.checkEvent(newEventID, newEventType)) {
           return "The new event has already registered";
       }
       if (!customer.getOtherCitynumforSwap(newEventID,oldEventID)) {
           return "If you swap you will register more than 3 other city event";
       }

       if (checkBranch(newEventID) && checkBranch(oldEventID)) {
           if (!swapLocalEvent(cutomerID, newEventID, oldEventID)) {
               return newEventID + " " + "is full";
           }
           customer.swapEvent(oldEventType, oldEventID, newEventType, newEventID);
           return "Success";

       }
       String[] param = {"swap", cutomerID, newEventID, oldEventID};
       String res = this.udpHandler.send(dataProcess(param), oldEventID.substring(0,3));
       if (res.equals("Success")) {
           customer.swapEvent(oldEventType, oldEventID, newEventType, newEventID);
       }
       return res;
  }

  private boolean swapLocalEvent (String customerID, String newEventID, String oldEventID) {
       Event newEvent = this.searchEvent(newEventID);
       Event oldEvent = this.searchEvent(oldEventID);
       if (!oldEvent.requestLocalSwap(newEvent, customerID)) {
           return false;
       }
       return true;
  }

  public String swapEventCancelRequest(String cutomerID, String newEventID, String oldEventID) {
       if (checkBranch(newEventID)) {
           if (!swapLocalEvent(cutomerID, newEventID, oldEventID)) {
               return newEventID + " " + "is full";
           }
           return "Success";
       }
       String[] param = {"swap_book", cutomerID, newEventID, oldEventID};
       String res = this.udpHandler.send(dataProcess(param), newEventID.substring(0,3));
       return res;
  }

  public String swapEventBookRequest(String cutomerID, String newEventID, String oldEventID) {
       Event event = this.searchEvent(newEventID);
       synchronized (event) {
           if (!event.isAvailable()) {
               return "Ths event is full";
           }
           String[] param = {"swap_cancel", cutomerID, oldEventID};
           String res = this.udpHandler.send(dataProcess(param), oldEventID.substring(0,3));
           if (res.equals("Success")) {
               event.swapBook(cutomerID);
           }
           return res;

       }
  }
  public String swapEventCancelReply(String cutomerID, String oldEventID) {
       Event event = this.searchEvent(oldEventID);
       if (event == null) {
           return "The event has been removed";
       }
       if (event.cancel(cutomerID)) {
           return "Success";
       }else {
           return "The event has been removed";
       }
  }


  //True : has this event, False: no
  private boolean checkEvent(String eventID, String eventType) {
       return EventSchedule.get(eventType).get(eventID) != null;
  }
  private boolean checkEventType (String eventType) {
       return EventSchedule.get(eventType) != null;
  }

  private boolean checkBranch(String eventID) {
       return branchName.equals(eventID.substring(0,3));
  }
  private boolean checkEventID(String eventID) {
        if (eventID.matches("([A-Z]{3}A)(\\d{6})") || eventID.matches("([A-Z]{3}M)(\\d{6})") || eventID.matches("([A-Z]{3}E)(\\d{6})")) {
            return true;
        }
        return false;
    }

  private String dataProcess(String[] data) {
       StringBuilder request = new StringBuilder();
       for (String s : data) {
           request.append(s + " ");
       }
      System.out.println(request.toString());
      return request.toString();
  }
}
