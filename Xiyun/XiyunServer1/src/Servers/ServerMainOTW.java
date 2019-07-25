package Servers;

public class ServerMainOTW {
    public static void main(String[] args){
        System.out.println("Starting OTW server...");
        Branch branch = Branch.OTW;
        Server server = new Server(branch);
        server.start();



    }

}
