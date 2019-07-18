package servers.distributed;

import models.EventType;
import models.Events;

import java.util.HashMap;

public class Ottawa {
    private static HashMap<EventType, Events> events = new HashMap<>();

    public static HashMap<EventType, Events> getEvents() {
        return events;
    }
}
