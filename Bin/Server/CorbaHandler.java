package Server;

import Server.Service;
import common.Log;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import serviceInterface.ServiceInterface;
import serviceInterface.ServiceInterfaceHelper;

public class CorbaHandler {

    private int port;
    private String city;
    private Service service;
    private Log log;
    private String message;

    public CorbaHandler(int port, String city, Service service, Log log) {
        this.port = port;
        this.city = city;
        this.service = service;
        this.log = log;
        this.message = "\nIn RMI handler: ";

    }
    public void register() {
        try {
            ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "1050"}, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(this.service);
            ServiceInterface operations = ServiceInterfaceHelper.narrow(ref);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

//            NameComponent[] ncPort = new NameComponent[2];
//            ncPort[0] = new NameComponent(Integer.toString(this.port), "");
//            ncPort[1] = new NameComponent(this.department, "");
//            ncRef.rebind(ncPort, operations);
            NameComponent path[] = ncRef.to_name(Integer.toString(this.port));
            ncRef.rebind(path, operations);
            System.out.println(this.city + " Server is running . . . ");
            orb.run();
        } catch (Exception e) {
            System.out.println ("Exception: " + e.getMessage());
        }

        System.out.println("HelloServer Exiting ...");
    }


}
