package FE;


import java.io.IOException;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import FrontEnd.FrontEndCorba;
import FrontEnd.FrontEndCorbaHelper;

public class FrontEnd {
    private FrontEndImpl service;
//    private Log log;
    public FrontEnd(FrontEndImpl service) {
        this.service = service;

    }
    public void register() {
        try {
            ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "1050"}, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            FrontEndImpl demsImpl = this.service;
            demsImpl.setORB(orb);
            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(demsImpl);
            FrontEndCorba href = FrontEndCorbaHelper.narrow(ref);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent path[] = ncRef.to_name("FrontEnd");
            ncRef.rebind(path, href);
            System.out.println(" FrontEnd is running . . . ");
            orb.run();
        } catch (Exception e) {
            System.out.println ("Exception: " + e.getMessage());
        }

    }

    public static void main(String[] args) throws IOException {
        FrontEndImpl frontEnd = new FrontEndImpl();
        FrontEnd server = new FrontEnd(frontEnd);

        server.register();
    }


}
