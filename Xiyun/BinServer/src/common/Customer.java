package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Customer {
  private HashMap<String, ArrayList<String>> bookedEvent;//key: eventType, value: eventID
  private String cityBelongTo;

  public Customer(String customerID) {
    this.bookedEvent = new HashMap<>();
    this.cityBelongTo = customerID.substring(0,3);
    bookedEvent.put("Conference", new ArrayList<>());
    bookedEvent.put("Seminar", new ArrayList<>());
    bookedEvent.put("TradeShow", new ArrayList<>());
  }

//True if has this event
  public boolean checkEvent(String eventID, String eventType) {
    if (!bookedEvent.containsKey(eventType)) {
      return false;
    }
    ArrayList<String> events = bookedEvent.get(eventType);
    if (events == null) {
      return false;
    }
    if (events.size() == 0) {
      return false;
    }
    return events.contains(eventID);
  }

  //This function is used to check most 3 events in a month in other cities
  public int getOtherCitynum (String eventID) {
    HashMap<String, Integer> hashMap = new HashMap<>();
    for (String key : bookedEvent.keySet()) {
      for (String temp : bookedEvent.get(key)) {
        if (!temp.substring(0,3).equals(cityBelongTo)){
          String tempKey = temp.substring(6,10);
          if (!hashMap.containsKey(tempKey)){
            hashMap.put(tempKey,1);
          }
          else {
            int num = hashMap.get(tempKey);
            num += 1;
            hashMap.put(tempKey,num);
          }
        }
      }
    }
    if (!hashMap.containsKey(eventID.substring(6,10))){
      return 0;
    }
    else {
      return hashMap.get(eventID.substring(6,10));
    }
  }

  public boolean getOtherCitynumforSwap (String newEventID, String oldEventID) {
    if (newEventID.substring(0,3).equals(this.cityBelongTo)) {
      return true;
    }else {
      if (!oldEventID.substring(0,3).equals(cityBelongTo)) {
        return true;
      }else {
        if (getOtherCitynum(newEventID) < 3) {
          return true;
        }
      }
    }
    return false;
  }


  public void bookEvent(String eventID, String eventType){
    synchronized (this) {
      bookedEvent.get(eventType).add(eventID);
    }
  }

  public void cancelEvent(String eventID) {
    synchronized (this) {
      for (ArrayList<String> list : bookedEvent.values()) {
        list.remove(eventID);
      }
    }
  }
  public void swapEvent (String oldEventType, String oldEventID, String newEventType, String newEventID) {
    synchronized (this) {
      bookedEvent.get(oldEventType).remove(oldEventID);
      bookedEvent.get(newEventType).add(newEventID);
    }

  }

  public String getSchedule() {
    String schedule = "";
    for (String eventKey : bookedEvent.keySet()) {
      ArrayList<String> eventList = bookedEvent.get(eventKey);
      schedule += eventKey + " : " + eventList.stream().collect(Collectors.joining(",")) + "\n";

    }
    return schedule;
  }

  public HashMap<String, ArrayList<String>> getBookedEvent() {
    return bookedEvent;
  }
}
