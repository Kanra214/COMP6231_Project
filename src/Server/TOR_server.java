package Server;

import common.CityToPort;

public class TOR_server {

    public static void main(String[] args) {
        Server TORserver = new Server(CityToPort.map.get("TOR"),"TOR");
        TORserver.startThread();
    }

}
