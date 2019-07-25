package Servers;
public class ServerMainTOR {
    public static void main(String[] args){
        System.out.println("Starting TOR server...");
        Branch branch = Branch.TOR;
        Server server = new Server(branch);
        server.start();


    }

}
