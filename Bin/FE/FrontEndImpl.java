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
import org.omg.CORBA.ORB;


public class FrontEndImpl extends FrontEndCorbaPOA{
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    @Override
    public String addEvent(String eventID, String eventType, int bookingCapacity) {
        DatagramSocket socket = null;
        int count = 0;
        StringBuilder sb = new StringBuilder();

        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);

            sendRequest(sb.append("ClientId:")
                .append("addEvent")
                .append(eventID)
                .append(" ")
                .append(eventType)
                .append(" ")
                .append(bookingCapacity).toString());

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "addEvent method";
    }

    @Override
    public String removeEvent(String eventID, String eventType) {
        DatagramSocket socket = null;
        int count = 0;
        StringBuilder sb = new StringBuilder();

        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);

            sendRequest(sb.append("ClientId:")
                .append("removeEvent")
                .append(eventID)
                .append(" ")
                .append(eventType).toString());

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "removeEvent method";
    }

    @Override
    public String listEventAvailability(String eventType) {
        DatagramSocket socket = null;
        int count = 0;
        StringBuilder sb = new StringBuilder();

        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);

            sendRequest(sb.append("ClientId:")
                .append("listEventAvailability")
                .append(eventType).toString());

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "listEvent method";
    }

    @Override
    public String bookEvent(String customerID, String eventID, String eventType) {
        DatagramSocket socket = null;
        int count = 0;
        StringBuilder sb = new StringBuilder();

        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);

            sendRequest(sb.append("ClientId:")
                .append("bookEvent")
                .append(" ")
                .append(customerID)
                .append(" ")
                .append(eventID)
                .append(" ")
                .append(eventType).toString());

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "bookEvent method";
    }

    @Override
    public String getBookingSchedule(String customerID) {
        DatagramSocket socket = null;
        int count = 0;
        StringBuilder sb = new StringBuilder();

        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);

            sendRequest(sb.append("ClientId:")
                .append("getBookingSchedule")
                .append(" ")
                .append(customerID).toString());

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "getBooking method";
    }

    @Override
    public String cancelEvent(String customerID, String eventID, String eventType) {
        DatagramSocket socket = null;
        int count = 0;
        StringBuilder sb = new StringBuilder();

        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);

            sendRequest(sb.append("ClientId:")
                .append("cancelEvent")
                .append(" ")
                .append(customerID)
                .append(" ")
                .append(eventID)
                .append(" ")
                .append(eventType).toString());

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "cancelEvent method";
    }

    @Override
    public String swapEvent(String customerID, String newEventID, String newEventType,
        String oldEventID, String oldEventType) {
        DatagramSocket socket = null;
        int count = 0;
        StringBuilder sb = new StringBuilder();

        try {
            socket = new DatagramSocket(FEPort.FE_PORT.FEPort);

            sendRequest(sb.append("ClientId:")
                .append("swapEvent")
                .append(" ")
                .append(customerID)
                .append(" ")
                .append(newEventID)
                .append(" ")
                .append(newEventType)
                .append(" ")
                .append(oldEventID)
                .append(" ")
                .append(oldEventType).toString());

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
//
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
    private String registerListener(DatagramSocket socket) {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
            String result = new String(packet.getData(), 0 , packet.getLength());

            System.out.println("receive " + result);
            return result;


        } catch (SocketException e){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum Failure {
        SoftWareFailure,
        ServerCrash,
    }

}
