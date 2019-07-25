package Servers;

public class ServerMainMTL {
    public static void main(String[] args) {
        System.out.println("Starting MTL server...");
        Branch branch = Branch.MTL;
        Server server = new Server(branch);
        server.start();



    }

}
