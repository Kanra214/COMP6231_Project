public enum RMID {
    BinServer(6000, 6010, 6020),
    Xiyun1(6001,6011,6021),
    Xiyun2(6002,6012,6022);

    private int betweenSequencer,betweenFE, betweenRM;

    RMID(int betweenSequencer, int betweenFE, int betweenRM){
        this.betweenSequencer = betweenSequencer;
        this.betweenFE = betweenFE;
        this.betweenRM = betweenRM;
    }

    public int getBetweenSequencer() {
        return betweenSequencer;
    }

    public int getBetweenFE() {
        return betweenFE;
    }

    public int getBetweenRM() {
        return betweenRM;
    }

}
