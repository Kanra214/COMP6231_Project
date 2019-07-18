package Server;

import common.CityToPort;

public class MTL_server {

    public static void main(String[] args) {
        Server MTLserver = new Server(CityToPort.map.get("MTL"),"MTL");
        MTLserver.startThread();
    }

}
