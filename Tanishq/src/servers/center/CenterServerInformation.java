package servers.center;

import models.Location;

import java.net.InetAddress;
import java.util.Date;

public class CenterServerInformation {
    private String locationStr;
    private Location location;
    private int udpPort;
    private InetAddress inetAddress;
    private Date date;

    public CenterServerInformation(Location location, int udpPort, InetAddress inetAddress, Date date) {
        this.location = location;
        switch (location) {
            case MTL:
                this.locationStr = "Montreal";
                break;
            case OTW:
                this.locationStr = "Ottawa";
                break;
            case TOR:
                this.locationStr = "Toronto";
                break;
            default:
                //TODO: handle exception
        }
        this.udpPort = udpPort;
        this.inetAddress = inetAddress;
        this.date = date;
    }
}
