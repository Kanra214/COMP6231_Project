package Servers;
public enum Branch2 {


    TOR(7772, 7773),//TODO:hard code this

    MTL(8882, 8883),

    OTW(9992, 9993);




    private int udpSendPort,udpReplyPort;
    Branch2(int udpSendPort, int udpReplyPort){
        this.udpSendPort = udpSendPort;
        this.udpReplyPort = udpReplyPort;
    }
    public int getUdpSendPort(){
        return udpSendPort;
    }
    public int getUdpReplyPort(){
        return udpReplyPort;
    }
    public static Branch2 getBranchFromString(String s){
        switch(s){
            case "TOR":
                return TOR;
            case "MTL":
                return MTL;
            case "OTW":
                return OTW;
        }
        return null;
    }

    public static Branch2 getBranchFromPort(int port){
        switch(port){
            case 7772:
                return TOR;
            case 7773:
                return TOR;
            case 8882:
                return MTL;
            case 8883:
                return MTL;

            case 9992:
                return OTW;
            case 9993:
                return OTW;
        }
        return null;
    }
}
