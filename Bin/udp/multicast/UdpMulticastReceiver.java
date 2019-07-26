package udp.multicast;

import java.io.IOException;
import java.net.*;
/*
public class UdpMulticastReceiver implements Runnable{

    private Replica replica;
    public  boolean shouldStop = false;
    public UdpMulticastReceiver(Replica replica) throws UnknownHostException {
        this.replica = replica;
        new Thread(this).start();
    }

    public synchronized void setShouldStop(boolean value) {
        this.shouldStop = value;
    }

    public void run(){
        MulticastSocket receivingSocket = null;
        System.out.println("Listening to receive Multicast request from fronted");
        try{
            receivingSocket = new MulticastSocket(1234);
            InetAddress group = InetAddress.getByName("224.0.0.5");
            receivingSocket.joinGroup(group);
            while (!shouldStop){
                byte[] data = new byte[65000];
                DatagramPacket dataPacket = new DatagramPacket(data, data.length);
                try {
                    receivingSocket.receive(dataPacket);
                    Thread actionExecutor = new ReplicaUDPActionExecutor(replica, dataPacket);
                    actionExecutor.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(!receivingSocket.isClosed()) receivingSocket.close();
        }
    }
}*/