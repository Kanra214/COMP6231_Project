package servers.center;

import models.Location;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import utils.PropertyManager;

import java.io.IOException;

public class CenterServerManager {

    public static void main(String[] args) throws Exception {
        CenterServerImplementation mtlServer = new CenterServerImplementation(Location.MTL);
        CenterServerImplementation otwServer = new CenterServerImplementation(Location.OTW);
        CenterServerImplementation torServer = new CenterServerImplementation(Location.TOR);

        ORB orb = ORB.init(args, PropertyManager.getProperties());
        POA rootPoa = (POA) orb.resolve_initial_references("RootPOA");
        rootPoa.the_POAManager().activate();

        org.omg.CORBA.Object mtlCorbaRef = rootPoa.servant_to_reference(mtlServer);
        org.omg.CORBA.Object otwCorbaRef = rootPoa.servant_to_reference(otwServer);
        org.omg.CORBA.Object torCorbaRef = rootPoa.servant_to_reference(torServer);
        CenterServerIdl.CenterServer mtlRef = CenterServerIdl.CenterServerHelper.narrow(mtlCorbaRef);
        CenterServerIdl.CenterServer otwRef = CenterServerIdl.CenterServerHelper.narrow(otwCorbaRef);
        CenterServerIdl.CenterServer torRef = CenterServerIdl.CenterServerHelper.narrow(torCorbaRef);

        org.omg.CORBA.Object nameServiceCorbaRef = orb.resolve_initial_references("NameService");
        NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceCorbaRef);

        NameComponent[] mtlPath = nameService.to_name("MTLServer");
        NameComponent[] otwPath = nameService.to_name("OTWServer");
        NameComponent[] torPath = nameService.to_name("TORServer");
        nameService.rebind(mtlPath, mtlRef);
        nameService.rebind(otwPath, otwRef);
        nameService.rebind(torPath, torRef);
        //System.out.println("#----------------- Server is starting -----------------#");

        orb.run();
    }

    static {
        System.out.println("Starting CORBA on cmd.");
        try {
            Runtime.getRuntime().exec("orbd -ORBInitialPort 1050 -ORBInitialHost localhost");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Started CORBA on cmd.");
    }

}
