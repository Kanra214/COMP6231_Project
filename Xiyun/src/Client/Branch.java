package Client;

public enum Branch {
    TOR(7770, 7771, 1050),
    MTL(8880,8881, 1050),
    OTW(9990,9991, 1050);

    private int udpSendPort,udpReplyPort, orbPort;
    Branch(int udpSendPort, int udpReplyPort, int orbPort){
        this.udpSendPort = udpSendPort;
        this.udpReplyPort = udpReplyPort;
        this.orbPort = orbPort;
    }
    public int getUdpSendPort(){
        return udpSendPort;
    }
    public int getUdpReplyPort(){
        return udpReplyPort;
    }
    public int getOrbPort(){
        return orbPort;
    }
    public static Branch getBranchFromString(String s){
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

    public static Branch getBranchFromPort(int port){
        switch(port){
            case 7770:
                return TOR;
            case 7771:
                return TOR;
            case 8880:
                return MTL;
            case 8881:
                return MTL;

            case 9990:
                return OTW;
            case 9991:
                return OTW;
        }
        return null;
    }
}
