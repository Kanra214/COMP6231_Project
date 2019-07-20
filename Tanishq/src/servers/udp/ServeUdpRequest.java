package servers.udp;

import models.UDPRequestType;
import models.UDPResponseType;
import servers.center.CenterServerImplementation;
import utils.Constants;
import utils.LoggerUtility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServeUdpRequest extends Thread {
    private DatagramPacket receivedPacket;
    private CenterServerImplementation centerServerImplementation;
    private Logger logger;

    public ServeUdpRequest(DatagramPacket packet, CenterServerImplementation centerServerImplementation) throws IOException {
        receivedPacket = packet;
        this.centerServerImplementation = centerServerImplementation;
        initLogger();
    }

    @Override
    public void run() {
        UDPRequestType udpRequestType;
        String data = new String(receivedPacket.getData());
        /*if (data.contains(TRANSFER_RECORD_PREFIX)) {
            data = data.replace(TRANSFER_RECORD_PREFIX, "");
            udpRequestType = UDPRequestType.TRANSFER_RECORD;
        } else*/
            udpRequestType = UDPRequestType.valueOf(new String(receivedPacket.getData()).trim());
        try (DatagramSocket udpSocket = new DatagramSocket()) {
            logger.log(Level.INFO, String.format(Constants.UDP_REQUEST_RECEIVED, udpRequestType, receivedPacket.getAddress(),
                    receivedPacket.getPort()));
            if (udpRequestType == UDPRequestType.PING) {
                servePingRequest(udpSocket);
            } else {
                throw new Exception();
            }
            logger.log(Level.INFO, String.format(Constants.UDP_RESPONSE_SENT, udpRequestType, receivedPacket.getAddress(),
                    receivedPacket.getPort()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, udpRequestType.toString());
        }
    }

    private void servePingRequest(DatagramSocket udpSocket) throws IOException {
        udpSocket = new DatagramSocket();
        byte[] responseData = "Hi, How can I help you?".getBytes();
        sendPacket(udpSocket, responseData);
    }


    private void storeReceivedObject(DatagramSocket udpSocket, int bufferSize) throws IOException {
        DatagramPacket objectByteArray = receivePacket(udpSocket, bufferSize);

    }

    private int getObjectByteArrayLength(DatagramSocket udpSocket) throws IOException {
        DatagramPacket lengthInfoPacket = receivePacket(udpSocket, Constants.DEFAULT_UDP_BUFFER_SIZE);
        int objectLength = Integer.parseInt(lengthInfoPacket.getData().toString());
        sendOkResponse(udpSocket);
        return objectLength;
    }

    private void sendOkResponse(DatagramSocket udpSocket) throws IOException {
        byte[] responseData = UDPResponseType.OK.toString().getBytes();
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

    private void initLogger() throws IOException {
        logger = LoggerUtility.getLogger(centerServerImplementation.getLocation());
    }
}