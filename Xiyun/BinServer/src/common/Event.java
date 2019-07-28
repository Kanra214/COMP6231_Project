package BinServer.src.common;

import java.util.ArrayList;

public class Event {
    private String eventID;
    private int capacity;
    private ArrayList<String> registedCustomer;

    public Event(String eventID, int capacity){
      this.eventID = eventID;
      this.capacity = capacity;
      this.registedCustomer = new ArrayList<>();
    }

    public boolean isAvailable() {
      return capacity > registedCustomer.size();
    }

    public boolean register(String customerID) {
        synchronized (this) {
          if (isAvailable()) {
            this.registedCustomer.add(customerID);
            return true;
          }
        }
        return false;
      }

    public boolean cancel(String customerID) {
      synchronized (this) {
        return this.registedCustomer.remove(customerID);
      }
    }
    //没什么用
    public boolean requestLocalSwap(Event event, String customerID) {
        return event.getRequestLocationSwap(this, customerID);
    }

    public boolean getRequestLocationSwap (Event event, String cutomerID) {
        synchronized (this) {
            if (!this.isAvailable()) {
                return false;
            }
            event.cancel(cutomerID);
            return this.register(cutomerID);
        }
    }

    public void swapBook (String customerID) {
        this.registedCustomer.add(customerID);
    }

    public int getAvailableSpace() {
      return capacity - registedCustomer.size();
    }
    public String getEventID() {
      return eventID;
    }

    public ArrayList<String> getRegistedCustomer() {
      return registedCustomer;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
