package models;

import java.util.ArrayList;
import java.util.List;

public class EventDetails {
    private int bookingCapacity;
    private int bookedCapacity;
    private List<String> bookedBy = new ArrayList<>();

    public EventDetails(int bookingCapacity, int bookedCapacity, List<String> bookedBy) {
        this.bookingCapacity = bookingCapacity;
        this.bookedCapacity = bookedCapacity;
        this.bookedBy = bookedBy;
    }

    public int getBookingCapacity() {
        return bookingCapacity;
    }

    public void setBookingCapacity(int bookingCapacity) {
        this.bookingCapacity = bookingCapacity;
    }

    public int getBookedCapacity() {
        return bookedCapacity;
    }

    public void setBookedCapacity(int bookedCapacity) {
        this.bookedCapacity = bookedCapacity;
    }

    public List<String> getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(List<String> bookedBy) {
        this.bookedBy = bookedBy;
    }

    @Override
    public String toString() {
        return " booking capacity: " + bookingCapacity
                + ", booked: " + bookedCapacity
                + ", booked by: " + bookedBy.toString();
    }
}
