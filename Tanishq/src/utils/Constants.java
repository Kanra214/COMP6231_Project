package utils;

public class Constants {
    //public static final String PROJECT_DIR = "W:\\IdeaProjects\\Java\\Projects\\FinalDEMS";
    public static final String LOG_DIR = "LOGS";
    public static final String BACKUP_DIR_NAME = "DATA_BACKUP";

    public static final int ORB_INITIAL_PORT = 1050;
    public static final String ORB_INITIAL_HOST = "localhost";

    public static final int OPEN_PORT_MIN_VAL = 49152;
    public static final int OPEN_PORT_MAX_VAL = 65535;
    public static final int PORT_CHECK_RETRY_VAL = 16;

    public static final String ADD_EVENT_SUCCESSFUL = "The event has been added.";
    public static final String ADD_EVENT_FAILURE = "Fail to add the event.";
    public static final String BOOK_EVENT_SUCCESSFUL = "The event has been booked.";
    public static final String BOOK_EVENT_FAILURE = "Fail to book the event.";
    public static final String BOOK_SCHEDULE_FAILURE = "Unable to fetch the booking schedule.";
    public static final String BOOK_SCHEDULE_NO_EVENTS = "No booked events found.";
    public static final String NO_EVENT_FOUND = "There is no event present with that event id.";
    public static final String REMOVE_EVENT_FAILURE = "Unable to remove the specified event.";
    public static final String REMOVE_EVENT_SUCCESSFUL = "The event has been removed.";
    public static final String LIST_EVENT_AVAILABILITY_FAILURE = "Unable to list the events.";
    public static final String CANCEL_EVENT_FAILURE = "Unable to cancel the event.";
    public static final String CANCEL_EVENT_SUCCESSFUL = "The event has been cancelled.";
    public static final String SWAP_EVENT_FAILURE = "Unable to swap the event.";
    public static final String SWAP_EVENT_SUCCESSFUL = "The swap was successful.";
    public static final String MORE_THAN_3_EVENTS = "You can only book three events in a month.";

    public static String SOCKET_CONNECTION_ERROR = "Could not connect to the port {0}, please try again.";
    public static String UDP_SERVER_STARTED = "UDP servers has been started and running for {0} data center.";
    public static String UDP_REQUEST_RECEIVED = "UDP %s request received from the address %s and port %s.";
    public static String UDP_RESPONSE_SENT = "UDP %s response sent to the address %s and port %s.";
    public static final int DEFAULT_UDP_BUFFER_SIZE = 100;
}