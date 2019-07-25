package Server;

import common.CityToPort;
import common.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UDPHandler {

  private int port;
  private EventService service;
  private Log log;

  public UDPHandler(int port, EventService service, Log log) {
      this.port = port;
      this.service = service;
      this.log = log;
      service.setUdpHandler(this);
      this.log.getLOGGER().info("\nUDP at port" + port + " started......");
      System.out.println("\nUDP at port" + port + " started......");
  }

  public String send(String data, String dest) {
      DatagramSocket aSocket = null;
      String Reply = null;
      System.out.println("Send" + data);
      System.out.println("\nUDP at port" + this.port + "Send request to" + CityToPort.map.get(dest) + ":\n" + data);
      byte [] message = data.getBytes();

      try {
        aSocket = new DatagramSocket();
          System.out.println(message.length);
        DatagramPacket request = new DatagramPacket(message,message.length, InetAddress.getByName("localhost"),CityToPort.map.get(dest));
        aSocket.send(request);
        byte[] buffer = new byte[2048];
        DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
        aSocket.receive(reply);
        Reply = new String(buffer, 0, reply.getLength());
        System.out.println("Reply: " + Reply );
        aSocket.close();

      } catch (SocketException e) {
        e.printStackTrace();
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      this.log.getLOGGER().info("\nUDP at port : " + this.port + "get reply from" + CityToPort.map.get(dest) + "is: " + Reply);
      System.out.println("\nUDP at port : " + this.port + "get reply from" + CityToPort.map.get(dest) + "is: " + Reply );
      return Reply;
  }

  public void listen() {
      this.log.getLOGGER().info("\nUDP at Port :" + port + " listening.....");
      System.out.println("\nUDP at Port :" + port + " listening.....");
      int count = 0;
      DatagramSocket aSocket = null;
      try {
          aSocket = new DatagramSocket(this.port);
          byte[] buffer = new byte[2048];
          while(true) {
              count++;
              DatagramPacket request = new DatagramPacket(buffer, buffer.length);
              aSocket.receive(request);
              String received_data = new String(buffer, 0 , request.getLength());
              System.out.println("The Server received: " + received_data);
              DatagramSocket finalSocket = aSocket;
              new Thread(() -> {
                  try {
                      handle(finalSocket, request, buffer );
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }).start();
//              this.handle(finalSocket,request,buffer);
              System.out.println("The number of connect" + count);
          }
      } catch (SocketException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }finally {
          if (aSocket != null) {
              aSocket.close();
          }
      }
  }

  private void handle(DatagramSocket datagramSocket,DatagramPacket datagramPacket, byte[] buffer)
      throws IOException {
      String received_data = new String(buffer,0 ,datagramPacket.getLength());
      this.log.getLOGGER().info("\nUDP at port: " + this.port + " Received requst from " + datagramPacket.getPort() + ": " + received_data + "is handling....");
      System.out.println("\nUDP at port: " + this.port + " Received requst from " + datagramPacket.getPort() + ": " + received_data + "is handling....");
      String[] request = received_data.split("\\s+");
      String temp = request[0];
      System.out.println(temp);
      String res = "Failure";
      switch (request[0]) {
          case "book":
              res = this.service.bookEvent(request[1],request[2],request[3]);
              break;
          case "cancel":
              res = this.service.cancelEvent(request[1],request[2],request[3]);
              break;
          case "removeEvent":
              this.service.removeCutomerEvent(request);
              break;
          case "showAvailableEvents":
              res = this.service.ListEventAvailabilityInLocal(request[1]);
              break;
          case "swap" :
              res = this.service.swapEventCancelRequest(request[1],request[2],request[3]);
              break;
          case "swap_book" :
              res = this.service.swapEventBookRequest(request[1],request[2],request[3]);
              break;
          case "swap_cancel" :
              res = this.service.swapEventCancelReply(request[1],request[2]);//有问题
          default:
              System.out.println("There is error in UDP handle method");
      }
      byte[] message = res.getBytes();
      DatagramPacket reply = new DatagramPacket(message,message.length,datagramPacket.getAddress(),datagramPacket.getPort());
      this.log.getLOGGER().info("\n Finish handle...\n" + "UDP at port " + this.port + " Send reply to : " + datagramPacket.getPort() + ": \n" + res);
      System.out.println("\n Finish handle...\n" + "UDP at port " + this.port + " Send reply to : " + datagramPacket.getPort() + ": \n" + res);
      datagramSocket.send(reply);


  }


}
