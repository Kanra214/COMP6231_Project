package Client;

public class IDAnalyse {

    private String clientID;
    public IDAnalyse (String clientID) {
        this.clientID = clientID;
    }

    public boolean checkID() {
        if(!checkLength()){
            return false;
        }
        if (!checkDigit()) {
            return false;
        }
        if (!checkCity()) {
            return false;
        }
        if (!checkOccupation()) {
            return false;
        }
        return true;
    }
    private boolean checkLength(){
        if (this.clientID.length() != 8) {
            System.out.println("ID Length is not correct");
            return false;
        }
        return true;
    }

    private boolean checkCity() {
        if (this.clientID.matches("TOR(\\w{5})") || this.clientID.matches("MTL(\\w{5})") || this.clientID.matches("OTW(\\w{5})")){
            return  true;
        }else{
            System.out.println("ID Format is wrong, We just have TOR, MTL, OTW 3 branches");
            return false;
        }
    }

    private boolean checkOccupation() {
        if (this.clientID.matches("([A-Z]{3}C)(\\d{4})") || this.clientID.matches("([A-Z]{3}M)(\\d{4})")) {
            return true;
        }
        System.out.println("ID Format is wrong, you need to use C or M to describe your Identity");
        return false;
    }

    private boolean checkDigit() {
        if (this.clientID.matches("([A-Z]{4})(\\d{4})")) {
            return true;
        }
        System.out.println("ID Format is wrong, YOU SHOULD TYPE ONLY 4 DIGIT TO PRESENT YOUR ID, FOUR CHARACTERS TO PRESENT YOUR DEPARTMENT, AND ONE CHARACTER TO PRESENT YOUR JOB");
        return false;
    }

    public String[] analyseID() {
        String[] result = {this.clientID, analyseBranch(), analyseIdentity()};
        return result;
    }

    private String analyseIdentity() {
        if (this.clientID.matches("([A-Z]{3}M)(\\d{4})")) {
            return  "Manager";
        } else if (this.clientID.matches("([A-Z]{3}C)(\\d{4})")) {
            return  "Customer";
        }
        return "Nobody";
    }

    private String analyseBranch() {
        if (this.clientID.matches("TOR(\\w{5})")){
            return "TOR";
        } else if (this.clientID.matches("MTL(\\w{5})")) {
            return "MTL";
        } else if (this.clientID.matches("OTW(\\w{5})")) {
            return "OTW";
        }
        return "Undefined";
    }

    public void setID(String ID) {
        this.clientID = ID;
    }


}
