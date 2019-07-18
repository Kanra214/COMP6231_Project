package Server;
import  common.CityToPort;

public class OTW_server {

    public static void main(String[] args) {
        Server OTWserver = new Server(CityToPort.map.get("OTW"),"OTW");
        OTWserver.startThread();
    }

}
