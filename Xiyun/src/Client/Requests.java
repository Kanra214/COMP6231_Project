package Client;

public enum Requests {


        AddEvent, RemoveEvent, ListEventAvailability,

        BookEvent, CancelEvent, GetBookingSchedule,SwapEvent,

        StartServer, CreateClient, ModifyEventList, GetEventList, ModifyClientList;

        public static Requests getRequest(String s) {

                for (Requests r : Requests.values()) {
                        if (r.name().equals(s)) {
                                return r;
                        }
                }
                return null;
        }




}
