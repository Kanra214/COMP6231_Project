package Servers;

public class ServerMainOTW {
    public static void main(String[] args){
        System.out.println("Starting OTW server...");
        Branch branch = Branch.OTW;
        XiyunServer server = new XiyunServer(branch, false);
        server.start();



    }

}
