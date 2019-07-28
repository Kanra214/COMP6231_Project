package Common;
public enum Requests {


        AddEvent, RemoveEvent, ListEventAvailability,

        BookEvent, CancelEvent, GetBookingSchedule,SwapEvent,

        RestartServer, FixServer,

        StartServer, CreateClient, ModifyEventList, GetEventList, ModifyClientList; //do not use requests on this line!

        public static Requests getRequest(String s) {

                for (Requests r : Requests.values()) {
                        if (r.name().equals(s)) {
                                return r;
                        }
                }
                return null;
        }




}
