package FE;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import PortInformation.SequencerPort;
import java.net.SocketException;

public class SequencerTest {
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

                String received_data = new String(buffer, 0 , request.getLength());
                System.out.println("The Server received: " + received_data);

//                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),
//                    request.getPort());// reply packet ready
//
//                aSocket.send(reply);// reply sent
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

}
