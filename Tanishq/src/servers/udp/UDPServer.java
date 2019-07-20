package servers.udp;

import servers.center.CenterServerImplementation;
import utils.Constants;
import utils.LoggerUtility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer extends Thread {
    private CenterServerImplementation centerServerImplementation;
    private int udpPort;
    private Logger logger;
    private DatagramSocket udpSocket = null;

    public UDPServer(CenterServerImplementation centerServerImplementation) throws IOException {
        this.centerServerImplementation = centerServerImplementation;
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
        Random random = new Random();
        boolean caughtOpenPort = false;
        int retryCount = Constants.PORT_CHECK_RETRY_VAL;

        while (!caughtOpenPort && retryCount > 0) {
            udpPort = random.nextInt(Constants.OPEN_PORT_MAX_VAL - Constants.OPEN_PORT_MIN_VAL + 1) + Constants.OPEN_PORT_MIN_VAL;
            try {
                udpSocket = new DatagramSocket(udpPort);
                caughtOpenPort = true;
            } catch (IOException e) {
                logger.log(Level.SEVERE, Constants.SOCKET_CONNECTION_ERROR, udpPort);
                logger.log(Level.SEVERE, e.getMessage());
            }
            --retryCount;
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName("UDP Server: " + centerServerImplementation.getLocation());
        logger.log(Level.INFO, Constants.UDP_SERVER_STARTED, centerServerImplementation.getLocation());
        byte[] packetBuffer;
        DatagramPacket packet;
        try {
            while (true) {
                try {
                    packetBuffer = new byte[100];
                    packet = new DatagramPacket(packetBuffer, packetBuffer.length);
                    udpSocket.receive(packet);
                    //Start a new thread on each request
                    new ServeUdpRequest(packet, centerServerImplementation).start();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        } finally {
            if (udpSocket != null) udpSocket.close();
        }
    }

    private void initLogger() throws IOException {
        logger = LoggerUtility.getLogger(centerServerImplementation.getLocation());
    }
}
