package models;

import java.util.HashMap;

public class Events {
    private HashMap<String, EventDetails> events;

    public Events(HashMap<String, EventDetails> events) {
        this.events = events;
    }

    public HashMap<String, EventDetails> getEvents() {
        return events;
    }

    public void setEvents(HashMap<String, EventDetails> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return events.toString();
    }
}
