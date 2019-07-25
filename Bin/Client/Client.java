package Client;
import common.CityToPort;
import common.Log;
import FrontEnd.FrontEndCorba;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import FrontEnd.FrontEndCorbaHelper;


public class Client {
  protected String clientID;
  protected String city;
  protected FrontEndCorba service;
  protected Log log;
  private String Identify;
  public Client(String[] information) {
    this.clientID = information[0];
    this.city = information[1];
    this.Identify = information[2];
    this.log = new Log(clientID + "_client");
    this.Connect();
  }

  public void Connect() {
    try{
      // create and initialize the ORB
      ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "1050"}, null);

      // get the root naming context
      org.omg.CORBA.Object objRef =
          orb.resolve_initial_references("NameService");
      // Use NamingContextExt instead of NamingContext,
      // part of the Interoperable naming Service.
      NamingContextExt ncRef =
          NamingContextExtHelper.narrow(objRef);

      // resolve the Object Reference in Naming
      String name = "FrontEnd";
      this.service = FrontEndCorbaHelper.narrow(ncRef.resolve_str(name));
    }
    catch (Exception e) {
      System.out.println("ERROR : " + e) ;
      e.printStackTrace(System.out);
    }
  }

  public String bookEvent(String customerID, String eventID, String eventType) {
    String[] params = new String[3];
    params[0] = customerID;
    params[1] = eventID;
    params[2] = eventType;
    this.log.requestInfo("bookEvent", params);
    String status = null;
    status= this.service.bookEvent(customerID,eventID,eventType);
    this.log.info("bookEvent", params,status);
//    System.out.println(status);
    return status;
  }

  public String getBookingSchedule(String customerID) {
    String[] params = new String[3];
    params[0] = customerID;
    this.log.requestInfo("getBookingSchedule", params);
    String status = null;
    status = this.service.getBookingSchedule(customerID);
    this.log.info("getBookingSchedule", params,status);
//    System.out.println(status);
    return status;
  }

  public String cancelEvent(String customerID, String eventID, String eventType) {
    String[] params = new String[3];
    params[0] = customerID;
    params[1] = eventID;
    params[2] = eventType;
    String status = null;
    log.requestInfo("cancelEvent",params);
    status = this.service.cancelEvent(customerID,eventID,eventType);

//    System.out.println(status);
    log.info("cancelEvent",params,status);
    return status;

  }
  public String swapEvent(String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType) {
    String [] params = new String[5];
    params[0] = customerID;
    params[1] = newEventID;
    params[2] = newEventType;
    params[3] = oldEventID;
    params[4] = oldEventType;
    log.requestInfo("swapEvent", params);
    String status = this.service.swapEvent(customerID, newEventID, newEventType, oldEventID, oldEventType);
    log.info("swapEvent", params, status);
    System.out.println(status);
    return status;
  }
  public String getClientID() {
    return clientID;
  }

  public String getIdentify() {
    return Identify;
  }


}
