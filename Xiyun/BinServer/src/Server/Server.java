package Server;

import common.Log;
import java.rmi.RemoteException;

public class Server {

    private int port;
    private String branch;
    private Service service;
    private EventService eventService;
    private Log log;

    public Server (int port, String branch) {
        this.port = port;
        this.branch = branch;
        this.log = new Log(branch + "_server");
        this.eventService = new EventService(branch);
        this.service = new Service(this.eventService,this.log);

    }
    public void startThread() {
//        Thread rmiThread = new Thread(() -> this.startRMI());
        Thread udpThread = new Thread(() -> this.startUDP());
//        rmiThread.start();
        udpThread.start();
    }

//    private void startRMI(){
//        CorbaHandler corba = new CorbaHandler(this.port, this.branch, this.service, this.log);
//        corba.register();
//    }

    private void startUDP() {
        UDPHandler udp = new UDPHandler(this.port,this.eventService,this.log);
        udp.listen();
    }
}
