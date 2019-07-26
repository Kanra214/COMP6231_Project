package udp.multicast;

import udp.QueueBuffer;

import java.io.IOException;
import java.net.*;
import java.util.Random;

public class AcknowledgeMulticast implements Runnable{
    QueueBuffer ackQueue;
    String requestId;

    public AcknowledgeMulticast(QueueBuffer queue, String requestId){
        this.ackQueue = queue;
        this.requestId = requestId;
    }

    public void run(){
        DatagramSocket datagramSocket;
        try {
            datagramSocket = new DatagramSocket();
            byte[] data = requestId.getBytes();
            datagramSocket.send(new DatagramPacket(data, data.length,
                    InetAddress.getByName("localhost"), 12345));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
