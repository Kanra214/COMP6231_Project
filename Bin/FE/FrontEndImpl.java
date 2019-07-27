package FE;
import Common.JsonObject;
import Common.Requests;
import FrontEnd.FrontEndCorbaPOA;
import PortInformation.AddressInfo;
import PortInformation.FEPort;
import PortInformation.SequencerPort;
import java.util.StringJoiner;
import org.omg.CORBA.ORB;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


public class FrontEndImpl extends FrontEndCorbaPOA{
    private ORB orb;
    private HashMap<String, Integer> softwareFailCounter;


    static public Map<String, String> portToReplicaManager = new HashMap<>();
    //key RM portNumber, Value Number of Replicamanger
    static {
        portToReplicaManager.put("6001", "1");
        portToReplicaManager.put("6002", "2");
        portToReplicaManager.put("6003", "3");
    }
    public FrontEndImpl(){
        softwareFailCounter = new HashMap<String,Integer>();
        softwareFailCounter.put("1",0);
        softwareFailCounter.put("2",0);
        softwareFailCounter.put("3",0);
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
            sendRequest(o.objectToString());
            Timer timer = new Timer(socket,false);
            Thread thread = new Thread(timer);
            thread.start();
            while(count < 3 && !timer.timeout) {
                count = registerListener(socket, resultSet);
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return majority(resultSet);
//        return "addEvent method";
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
            sendRequest(o.objectToString());
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
        return majority(resultSet);

//        return "removeEvent method";
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
            sendRequest(o.objectToString());
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
            tellRMCrash(resultSet);
        }

        return majority(resultSet);

//        return "listEvent method";
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
            sendRequest(o.objectToString());
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
            tellRMCrash(resultSet);
        }
        return majority(resultSet);

//        return "bookEvent method";
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
            sendRequest(o.objectToString());
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
            tellRMCrash(resultSet);
        }
//        return "getBooking method";
        return majority(resultSet);

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
            sendRequest(o.objectToString());
            sendRequest(o.objectToString());
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
            tellRMCrash(resultSet);
        }
//        return "cancelEvent method";
        return majority(resultSet);

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
            sendRequest(o.objectToString());
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
            tellRMCrash(resultSet);
        }

        return majority(resultSet);
    }

    private String majority(HashMap<String,JsonObject> resultSet) {
        String result = null;
        int countTrue = 0;
        int countFalse = 0;
        for (Map.Entry<String, JsonObject> entry : resultSet.entrySet()) {
            if (entry.getValue().getApproved().equals("True")) {
                countTrue = countTrue + 1;
            }else {
                countFalse = countFalse + 1;
            }
        }
        if (countTrue == 3 || countFalse == 3) {
            for (JsonObject temp : resultSet.values()) {
                result = temp.getResponse();
                break;
            }
            return result;
        }
        if (countTrue == 2) {
            for (JsonObject temp : resultSet.values()) {
                if (temp.getApproved().equals("True")) {
                    result = temp.getResponse();
                }
                break;
            }
            return result;

        }
        if (countFalse == 2) {
            for (JsonObject temp : resultSet.values()) {
                if (temp.getApproved().equals("False")) {
                    result = temp.getResponse();
                }
                break;
            }
            return result;

        }
        findSoftwareFail(countTrue, countFalse, resultSet);

        return result;
    }

    private void findSoftwareFail(int countTrue, int countFalse, HashMap<String, JsonObject> resultSet) {
        if (countFalse ==3 || countTrue == 3) {
            return;
        }
        String failServerNum = null;
        if (countTrue == 2) {
            for (Map.Entry<String, JsonObject> entry : resultSet.entrySet()){
                if (!entry.getValue().getApproved().equals("False")){
                    failServerNum = entry.getKey();
                }
            }

        }
        if(countFalse == 2) {
            for (Map.Entry<String, JsonObject> entry : resultSet.entrySet()){
                if (!entry.getValue().getApproved().equals("True")){
                    failServerNum = entry.getKey();
                }
            }
        }

        if (failServerNum != null){
            for (Map.Entry<String,Integer> entry:
                softwareFailCounter.entrySet()) {
                if (!entry.getKey().equals(failServerNum)){
                    entry.setValue(0);
                } else if(entry.getKey().equals(failServerNum)){
                    entry.setValue(entry.getValue() + 1);
                }
            }
        }
        if (softwareFailCounter.get(failServerNum) != null && softwareFailCounter.get(failServerNum) == 3){
            sendToRM(failServerNum);
        }
    }
    //This function is used to send request to sequencer
    public void sendRequest(String message) throws Exception {

        InetAddress address = InetAddress.getByName("localhost");

        byte[] data = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, SequencerPort.SEQUENCER_PORT.sequencerPort);

        DatagramSocket socket = new DatagramSocket();
        socket.send(sendPacket);

    }


    private void tellRMCrash(HashMap<String, JsonObject> resultSet) {
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
    }
}
