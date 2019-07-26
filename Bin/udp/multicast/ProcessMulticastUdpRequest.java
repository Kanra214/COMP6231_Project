package udp.multicast;

import Server.Service;
import models.Location;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.DatagramPacket;
import java.util.Arrays;

public class ProcessMulticastUdpRequest implements Runnable{
    Service servant;
    DatagramPacket dataPacket;
    public ProcessMulticastUdpRequest(Service servant, DatagramPacket dataPacket){
        this.servant = servant;
        this.dataPacket = dataPacket;
    }
    public void run(){
        //http://docs.oracle.com/javase/6/docs/api/java/util/Arrays.html#copyOfRange%28byte[],%20int,%20int%29
        Location location = Location.valueOf(new String(Arrays.copyOfRange(dataPacket.getData(), 0, 3)));
        JSONParser parser = new JSONParser();
        //if(location == servant.serverLocation){
            byte[] data = Arrays.copyOfRange(dataPacket.getData(),3, dataPacket.getData().length);
            try {
                JSONObject jsondata = (JSONObject) parser.parse(data.toString());
                String requestId = jsondata.get("requestId").toString();

            } catch (ParseException e) {
                e.printStackTrace();
            }

        //}
        //else{
          //  System.out.println("Ignore request");
        //}
    }
}
