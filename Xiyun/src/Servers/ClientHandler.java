package Servers;
import MyApp.HandlerPOA;
import org.omg.CORBA.ORB;

public class ClientHandler extends HandlerPOA {
    private Server server;
    private ORB orb;

    public ClientHandler(Server server){
        this.server = server;

    }
    public void setORB(ORB orb_val){
        orb = orb_val;
    }

    @Override
    public String addEvent(String id, String eid, String et, int capacity) {
        return MyJsonObject.objectToString(server.addEvent(id, eid, et, capacity));
    }

    @Override
    public String removeEvent(String id, String eid, String et) {
        return MyJsonObject.objectToString(server.removeEvent(id, eid, et));
    }

    @Override
    public String listEventAvailability(String id, String et) {
        return MyJsonObject.objectToString(server.listEventAvailability(id, et));
    }

    @Override
    public String bookEvent(String cid, String mid, String eventId, String et) {
        return MyJsonObject.objectToString(server.bookEvent(cid, mid, eventId, et));
    }

    @Override
    public String cancelEvent(String cid, String mid, String eventId, String et) {
        return MyJsonObject.objectToString(server.cancelEvent(cid, mid, eventId, et));
    }

    @Override
    public String getBookingSchedule(String id, String mid) {
        return MyJsonObject.objectToString(server.getBookingSchedule(id, mid));
    }

    @Override
    public String authenEntry(String id, String req) {
        return  MyJsonObject.objectToString(server.authenEntry(id, Requests.getRequest(req)));
    }

    @Override
    public String getRequestList(String id) {
        Requests[] reqs = server.getRequestList(id);
        String str = "";
        for(Requests req : reqs){
            str += req.name() + ";";
        }
        return str;
    }

    @Override
    public String signup(String id) {
        return MyJsonObject.objectToString(server.createClient(id));
    }

    @Override
    public String login(String id) {
        return MyJsonObject.objectToString(server.login(id));
    }

    @Override
    public String swapEvent(String cid, String mid, String neid, String net, String oeid, String oet) {
        return MyJsonObject.objectToString(server.swapEvent(cid, mid, neid, net, oeid, oet));
    }

    @Override
    public void shutdown() {
        orb.shutdown(false);
    }

}
