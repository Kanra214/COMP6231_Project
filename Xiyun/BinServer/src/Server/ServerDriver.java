package Server;

import common.CityToPort;
import java.util.ArrayList;

public class ServerDriver {

    public static void main(String[] args) {
        ArrayList<Server> list = new ArrayList<>();
        for(String city : CityToPort.map.keySet()){
            list.add(new Server(CityToPort.map.get(city),city));
        }

        for (Server server : list) {
            server.startThread();
        }

    }

}
