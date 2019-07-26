package udp.unicast;

import Server.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UdpServer extends Thread {
    private Service service;
    private int udpPort;
    private Logger logger;
    private DatagramSocket udpSocket = null;
    public  boolean shouldStop = false;
    private String replicaName;

    public UdpServer(Service service, String replicaName) throws IOException {
        this.service = service;
        this.replicaName = replicaName;
        initLogger();
        decideUdpServerPort();
    }

    public int getUdpPort() {
        return udpPort;
    }

    public InetAddress getInetAddress() {
        try {
            return InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void decideUdpServerPort() {
        Random rnd = new Random();
        boolean caughtOpenPort = false;
        int retryCount = 20;

        while (!caughtOpenPort && retryCount > 0 && !shouldStop) {
            udpPort = rnd.nextInt(65535 - 49152 + 1) + 49152;
            try {
                udpSocket = new DatagramSocket(udpPort);
                caughtOpenPort = true;
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not connect to port ", udpPort);
                logger.log(Level.SEVERE, e.getMessage());
            }
            retryCount--;
        }
    }

    /**
     * run method of thread.
     * It accepts a UDP request and delegates it to ServerUdpRequest object
     */
    @Override
    public void run() {
        Thread.currentThread().setName("UDP Server: " /*+ service.serverLocation.toString()*/);
        //logger.log(Level.INFO, LogMessages.UDP_SERVER_STARTED, service.serverLocation + " " + udpPort);
        byte[] packetBuffer;
        DatagramPacket packet;
        try {
            while (!shouldStop) {
                try {
                    packetBuffer = new byte[100];
                    packet = new DatagramPacket(packetBuffer, packetBuffer.length);
                    udpSocket.receive(packet);
                    //Start a new thread on each request
                    new ServeUdpRequest(packet, service, replicaName).start();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        } finally {
            if (udpSocket != null) udpSocket.close();
        }
    }

    private void initLogger() throws IOException {
        //logger = LoggerProvider.getLogger(service.serverLocation, replicaName);
    }

    public synchronized void setShouldStop(boolean value) {
        this.shouldStop = value;
    }
}