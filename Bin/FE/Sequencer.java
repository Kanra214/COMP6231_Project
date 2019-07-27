package FE;

import java.io.IOException;
import java.net.*;

import PortInformation.AddressInfo;
import PortInformation.SequencerPort;

public class Sequencer {
    public static void main(String[] args) {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(SequencerPort.SEQUENCER_PORT.sequencerPort);
            byte[] buffer = new byte[1000];// to stored the received data from
            // the client.
            System.out.println("Sequencer Started............");
            while (true) {// non-terminating loop as the server is always in listening mode.
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);

                // Server waits for the request to come
                aSocket.receive(request);// request received

                String received_data = new String(buffer, 0, request.getLength());
                System.out.println("The Server received: " + received_data);
                Counter counter = new Counter(0);
                received_data = received_data + "SequenceNumber:" + counter.nextVal() + ";";
                System.out.println(received_data);

                sendToRM(received_data);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }

    private static void sendToRM(String sequencer_data) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();

            byte[] data = sequencer_data.getBytes();

            InetAddress address = InetAddress.getByName(AddressInfo.ADDRESS_INFO.RM1address);
            DatagramPacket packet = new DatagramPacket(data, 0, data.length, address, 6001);
            socket.send(packet);
            address = InetAddress.getByName(AddressInfo.ADDRESS_INFO.RM2address);
            packet = new DatagramPacket(data, 0, data.length, address, 6002);
            socket.send(packet);
            address = InetAddress.getByName(AddressInfo.ADDRESS_INFO.RM3address);
            packet = new DatagramPacket(data, 0, data.length, address, 6003);
            socket.send(packet);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        socket.close();
    }
}
