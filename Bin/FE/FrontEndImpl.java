package FE;
import FrontEnd.FrontEndCorbaPOA;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import PortInformation.AddressInfo;
import PortInformation.SequencerPort;
import PortInformation.FEPort;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.ORB;
import Common.JsonObject;
import Common.Requests;


public class FrontEndImpl extends FrontEndCorbaPOA{
    private ORB orb;
    static public Map<String, String> portToReplicaManager = new HashMap<>();
    //key RM portNumber, Value Number of Replicamanger
    static {
        portToReplicaManager.put("6001", "1");
        portToReplicaManager.put("6002", "2");
        portToReplicaManager.put("6003", "3");
    }

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    @Override
    public String addEvent(String ID, String eventID, String eventType, int bookingCapacity) {
        DatagramSocket socket = null;
        int count = 0;
        HashMap<String, JsonObject> resultSet = new HashMap<>();
        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);
            InetAddress addr = InetAddress.getLocalHost();
            JsonObject o = new JsonObject();
            o.setManagerId(ID);
            o.setEventId(eventID);
            o.setEventType(eventType);
            o.setCapacity(bookingCapacity);
            o.setRequest(Requests.AddEvent);
            o.setSourceIp(addr.getHostAddress());
            sendRequest(o.objectToString(o));
            Timer timer = new Timer(socket,false);
            Thread thread = new Thread(timer);
            thread.start();
            while(count < 3 && !timer.timeout) {
                count = registerListener(socket, resultSet);
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "addEvent method";
    }

    @Override
    public String removeEvent(String ID, String eventID, String eventType) {
        DatagramSocket socket = null;
        HashMap<String, JsonObject> resultSet = new HashMap<>();
        int count = 0;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            JsonObject o = new JsonObject();
            o.setManagerId(ID);
            o.setEventId(eventID);
            o.setEventType(eventType);
            o.setRequest(Requests.RemoveEvent);
            o.setSourceIp(addr.getHostAddress());
            sendRequest(o.objectToString(o));
            Timer timer = new Timer(socket,false);
            Thread thread = new Thread(timer);
            thread.start();
            while(count < 3 && !timer.timeout) {
                count = registerListener(socket, resultSet);
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
        if (resultSet.size() < 3){
//            tellRMCrash(resultSet);
        }
        return "removeEvent method";
    }

    @Override
    public String listEventAvailability(String ID, String eventType) {
        DatagramSocket socket = null;
        int count = 0;
        HashMap<String, JsonObject> resultSet = new HashMap<>();
        try {
            InetAddress addr = InetAddress.getLocalHost();
            JsonObject o = new JsonObject();
            o.setManagerId(ID);
            o.setEventType(eventType);
            o.setRequest(Requests.ListEventAvailability);
            o.setSourceIp(addr.getHostAddress());
            sendRequest(o.objectToString(o));
            Timer timer = new Timer(socket,false);
            Thread thread = new Thread(timer);
            thread.start();
            while(count < 3 && !timer.timeout) {
                count = registerListener(socket, resultSet);
            }


        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "listEvent method";
    }

    @Override
    public String bookEvent(String ID, String customerID, String eventID, String eventType) {
        DatagramSocket socket = null;
        int count = 0;
        HashMap<String, JsonObject> resultSet = new HashMap<>();
        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);

            InetAddress addr = InetAddress.getLocalHost();
            JsonObject o = new JsonObject();
            if (ID.substring(3,4).equals("M")) {
                o.setManagerId(ID);
            }else {
                o.setClientId(ID);
            }
            o.setClientId(customerID);
            o.setEventId(eventID);
            o.setEventType(eventType);
            o.setRequest(Requests.BookEvent);
            o.setSourceIp(addr.getHostAddress());
            sendRequest(o.objectToString(o));
            Timer timer = new Timer(socket,false);
            Thread thread = new Thread(timer);
            thread.start();
            while(count < 3 && !timer.timeout) {
                count = registerListener(socket, resultSet);
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "bookEvent method";
    }

    @Override
    public String getBookingSchedule(String ID, String customerID) {
        DatagramSocket socket = null;
        int count = 0;
        HashMap<String, JsonObject> resultSet = new HashMap<>();
        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);


            InetAddress addr = InetAddress.getLocalHost();
            JsonObject o = new JsonObject();
            if (ID.substring(3,4).equals("M")) {
                o.setManagerId(ID);
            }else {
                o.setClientId(ID);
            }
            o.setClientId(customerID);
            o.setRequest(Requests.GetBookingSchedule);
            o.setSourceIp(addr.getHostAddress());
            sendRequest(o.objectToString(o));
            Timer timer = new Timer(socket,false);
            Thread thread = new Thread(timer);
            thread.start();
            while(count < 3 && !timer.timeout) {
                count = registerListener(socket, resultSet);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "getBooking method";
    }

    @Override
    public String cancelEvent(String ID, String customerID, String eventID, String eventType) {
        DatagramSocket socket = null;
        int count = 0;
        HashMap<String, JsonObject> resultSet = new HashMap<>();
        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);
            InetAddress addr = InetAddress.getLocalHost();
            JsonObject o = new JsonObject();
            if (ID.substring(3,4).equals("M")) {
                o.setManagerId(ID);
            }else {
                o.setClientId(ID);
            }
            o.setClientId(customerID);
            o.setEventId(eventID);
            o.setEventType(eventType);
            o.setRequest(Requests.CancelEvent);
            o.setSourceIp(addr.getHostAddress());
            sendRequest(o.objectToString(o));
            sendRequest(o.objectToString(o));
            Timer timer = new Timer(socket,false);
            Thread thread = new Thread(timer);
            thread.start();
            while(count < 3 && !timer.timeout) {
                count = registerListener(socket, resultSet);
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "cancelEvent method";
    }

    @Override
    public String swapEvent(String ID, String customerID, String newEventID, String newEventType,
        String oldEventID, String oldEventType) {
        HashMap<String, JsonObject> resultSet = new HashMap<>();
        DatagramSocket socket = null;
        int count = 0;
        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);
            InetAddress addr = InetAddress.getLocalHost();
            JsonObject o = new JsonObject();
            if (ID.substring(3,4).equals("M")) {
                o.setManagerId(ID);
            }else {
                o.setClientId(ID);
            }
            o.setClientId(customerID);
            o.setEventId(newEventID);
            o.setEventType(newEventType);
            o.setRequest(Requests.SwapEvent);
            o.setOldEventId(oldEventID);
            o.setOldEventType(oldEventType);
            o.setSourceIp(addr.getHostAddress());
            sendRequest(o.objectToString(o));
            Timer timer = new Timer(socket,false);
            Thread thread = new Thread(timer);
            thread.start();
            while(count < 3 && !timer.timeout) {
                count = registerListener(socket, resultSet);
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "swapEvent method";
    }

    //This function is used to send request to sequencer
    public void sendRequest(String message) throws Exception {

        InetAddress address = InetAddress.getByName("localhost");

        byte[] data = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, SequencerPort.SEQUENCER_PORT.sequencerPort);

        DatagramSocket socket = new DatagramSocket();
        socket.send(sendPacket);

    }

//    private void multicastCrashMsg(DatagramSocket socket, byte[] data){
//        try {
//            socket.send(packet(AddressInfo.ADDRESS_INFO.RM1address, data,Replica.REPLICA.replica1));
//            socket.send(packet(AddressInfo.ADDRESS_INFO.RM2address, data,Replica.REPLICA.replica2 ));
//            socket.send(packet(AddressInfo.ADDRESS_INFO.RM3address, data,Replica.REPLICA.replica3));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void sendReq(String msg) {
//        try {
//            DatagramSocket socket = new DatagramSocket();
//            byte[] data = msg.getBytes();
//            multicastCrashMsg(socket,data);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//    }
    private void tellRMCrash(Map<String, String> resultSet) {
        if (!resultSet.containsKey("1")) {
            String msg = "1 " + Failure.ServerCrash;
            sendReq(msg);
        } else if (!resultSet.containsKey("2")) {
            String msg = "2 " + Failure.ServerCrash;
            sendReq(msg);
        } else if (!resultSet.containsKey("3")) {
            String msg = "3 " + Failure.ServerCrash;
            sendReq(msg);
        }
    }

    private void sendReq(String msg) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] data = msg.getBytes();
//            multicastCrashMsg(socket,data);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void sendToRM(String crashServerNum) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            String msg = Failure.SoftWareFailure.toString();

            byte[] data = msg.getBytes();

            if (crashServerNum.equals("1")){
                InetAddress address = InetAddress.getByName(AddressInfo.ADDRESS_INFO.RM1address);
                DatagramPacket packet = new DatagramPacket(data, 0, data.length,address,6001 );
                socket.send(packet);
            } else if (crashServerNum.equals("2")){
                InetAddress address = InetAddress.getByName(AddressInfo.ADDRESS_INFO.RM2address);
                DatagramPacket packet = new DatagramPacket(data, 0, data.length,address,6002);
                socket.send(packet);
            } else {
                InetAddress address = InetAddress.getByName(AddressInfo.ADDRESS_INFO.RM3address);
                DatagramPacket packet = new DatagramPacket(data, 0, data.length,address,6003 );
                socket.send(packet);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        socket.close();
    }
    private int registerListener(DatagramSocket socket, HashMap<String, JsonObject> resultSet) {
        JsonObject o;
        byte[] data = new byte[1024];
        String result;
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
            result = new String(packet.getData(), 0 , packet.getLength());
            System.out.println("receive " + result);
            o = JsonObject.stringToObject(result);
            resultSet.put(portToReplicaManager.get(o.getSourcePort()),o);


        } catch (SocketException e){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultSet.size();
    }

    enum Failure {
        SoftWareFailure,
        ServerCrash,
    }


    public enum Replica{
        REPLICA;
        public final int replica1 = 6001;
        public final int replica2 = 6002;
        public final int replica3 = 6003;
        public final int replica4 = 6004;
    }
}
