package Client;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    public static final String[] EVENTTYPES = {"Seminar", "Conference", "TradeShow"};

    private String id;
    private String type;

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private int capacity;
    private int scheduled;
    private char timeslot;
    private Date date;
    private Branch branch;
    private String details;

    public Event(int capacity, String eventId, String et, String details) {
        this.capacity = capacity;
        this.id = eventId;
        this.type = et;
        this.details = details;
        branch = Branch.getBranchFromString(id.substring(0, 3));
        timeslot = id.charAt(3);
        date = parseEventDate(id);
    }
    public static Date parseEventDate(String eid) {
        try {
            return new SimpleDateFormat("ddMMyy").parse(eid.substring(4,10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;


    }


    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public char getTimeslot() {
        return timeslot;
    }

    public Date getDate() {
        return date;
    }

    public Branch getBranch() {
        return branch;
    }

    public String getDetails() {
        return details;
    }
    public int getScheduled() {
        return scheduled;
    }
    public int getCapacity(){
        return capacity;
    }

    protected synchronized boolean increment() {
        if (capacity > scheduled) {
            scheduled++;
            return true;
        } else {
            return false;
        }
    }

    protected synchronized boolean decrement() {
        if(scheduled > 0){
            scheduled--;
            return true;
        }
        else{
            return false;
        }

    }
    public static String dateToString(Date date){
        Format f = new SimpleDateFormat("yyyy-MMM-dd");
        return f.format(date);

    }

}
