package udp.unicast;

import Server.Service;
import udp.UdpRequestType;
import udp.UdpResponseType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServeUdpRequest extends Thread {
    private DatagramPacket receivedPacket;
    private Service service;
    private Logger logger;
    private String replicaName;
    /**
     * @param packet       DatagramPacket which contains request message
     * @param service Center Server reference to server record count request
     * @throws IOException
     */
    public ServeUdpRequest(DatagramPacket packet, Service service, String replicaName) throws IOException {
        receivedPacket = packet;
        this.service = service;
        this.replicaName = replicaName;
        initLogger();
    }

    /**
     * Method which unmarshals request packet, processes it and replies back
     */
    @Override
    public void run() {
        UdpRequestType udpMessage;
        String data = new String(receivedPacket.getData());
        /*if (data.contains(some custom data))
        {
            data = data.replace(TRANSFER_RECORD_PREFIX,"");
            udpMessage = UdpRequestType.TRANSFER_RECORD;
        }
        else {*/
            udpMessage = UdpRequestType.valueOf(new String(receivedPacket.getData()).trim());
        //}
        DatagramSocket udpSocket = null;
        try {
            udpSocket = new DatagramSocket();
            //logger.log(Level.INFO, String.format(LogMessages.UDP_REQ_RECEIVED, udpMessage, receivedPacket.getAddress(),receivedPacket.getPort()));
            switch (udpMessage) {
                case PING:
                    servePingRequest(udpSocket);
                    break;
                /*case custom_data_to_be_passed:
                    custom_func(udpSocket);
                    break;*/
            }
            //logger.log(Level.INFO, String.format(LogMessages.UDP_RESPONSE_SENT, udpMessage, receivedPacket.getAddress(),receivedPacket.getPort()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (udpSocket != null) udpSocket.close();
        }
    }

    private void servePingRequest(DatagramSocket udpSocket) throws IOException {
        udpSocket = new DatagramSocket();
        byte[] responseData = "Hi, How can I help you?".getBytes();
        sendPacket(udpSocket, responseData);
    }
/*
    private void custom_func(DatagramSocket udpSocket) throws IOException {
        byte[] responseData = service.getRecordCount().toString().getBytes();
        sendPacket(udpSocket, responseData);
    }*/


    private void storeReceivedObject(DatagramSocket udpSocket, int bufferSize) throws IOException {
        DatagramPacket objectByteArray = receivePacket(udpSocket, bufferSize);

    }

    private int getObjectByteArrayLength(DatagramSocket udpSocket) throws IOException {
        DatagramPacket lengthInfoPacket = receivePacket(udpSocket, 100);
        int objectLength = Integer.parseInt(lengthInfoPacket.getData().toString());
        sendOkResponse(udpSocket);
        return objectLength;
    }

    private void sendOkResponse(DatagramSocket udpSocket) throws IOException {
        byte[] responseData = UdpResponseType.OK.toString().getBytes();
        sendPacket(udpSocket, responseData);
    }
    private void sendPacket(DatagramSocket udpSocket, byte[] responseData) throws IOException {
        udpSocket.send(new DatagramPacket(responseData, responseData.length, receivedPacket.getAddress(),
                receivedPacket.getPort()));
    }

    private DatagramPacket receivePacket(DatagramSocket udpSocket, int bufferSize) throws IOException {
        byte[] data = new byte[bufferSize];
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
        udpSocket.receive(datagramPacket);
        return datagramPacket;
    }

    /**
     * Initialize Logger instance
     *
     * @throws IOException
     */
    private void initLogger() throws IOException {
        //logger = LoggerProvider.getLogger(service.serverLocation, replicaName);
    }
}