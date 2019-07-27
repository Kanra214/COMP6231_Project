package Servers;
public class ServerMainTOR {
    public static void main(String[] args){
        System.out.println("Starting TOR server...");
        Branch branch = Branch.TOR;
        XiyunServer server = new XiyunServer(branch);
        server.start();


    }

}
