package servers.distributed;

import models.Events;
import models.EventType;

import java.util.HashMap;

public class Toronto {
    private static HashMap<EventType, Events> events = new HashMap<>();

    public static HashMap<EventType, Events> getEvents() {
        return events;
    }
}
