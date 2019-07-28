package RMs.src;

public enum RMID {
    BinServer(6000, 6010, 6020),
    Xiyun1(6001,6011,6021),
    Xiyun2(6002,6012,6022);



    private int listeningPort,replyingPort, betweenRM;

    RMID(int listeningPort, int replyingPort, int betweenRM){
        this.listeningPort = listeningPort;
        this.replyingPort = replyingPort;
        this.betweenRM = betweenRM;
    }
    public int getListeningPort() {
        return listeningPort;
    }

    public int getReplyingPort() {
        return replyingPort;
    }

    public int getBetweenRM() {
        return betweenRM;
    }


}
