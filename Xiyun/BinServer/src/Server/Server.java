package Server;

import common.Log;

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
        Thread udpThread = new Thread(() -> this.startUDP());
        udpThread.start();
    }

    private void startUDP() {
        UDPHandler udp = new UDPHandler(this.port,this.eventService,this.log);
        udp.listen();
    }
}
