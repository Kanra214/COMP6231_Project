package FrontEnd;


/**
* FrontEnd/FrontEndCorbaPOA.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从FrontEndCorba.idl
* 2019年7月25日 星期四 下午06时44分52秒 EDT
*/

public abstract class FrontEndCorbaPOA extends org.omg.PortableServer.Servant
 implements FrontEnd.FrontEndCorbaOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("addEvent", new java.lang.Integer (0));
    _methods.put ("removeEvent", new java.lang.Integer (1));
    _methods.put ("listEventAvailability", new java.lang.Integer (2));
    _methods.put ("bookEvent", new java.lang.Integer (3));
    _methods.put ("getBookingSchedule", new java.lang.Integer (4));
    _methods.put ("cancelEvent", new java.lang.Integer (5));
    _methods.put ("swapEvent", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // FrontEnd/FrontEndCorba/addEvent
       {
         String ID = in.read_string ();
         String eventID = in.read_string ();
         String eventType = in.read_string ();
         int bookingCapacity = in.read_long ();
         String $result = null;
         $result = this.addEvent (ID, eventID, eventType, bookingCapacity);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // FrontEnd/FrontEndCorba/removeEvent
       {
         String ID = in.read_string ();
         String eventID = in.read_string ();
         String eventType = in.read_string ();
         String $result = null;
         $result = this.removeEvent (ID, eventID, eventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // FrontEnd/FrontEndCorba/listEventAvailability
       {
         String ID = in.read_string ();
         String eventType = in.read_string ();
         String $result = null;
         $result = this.listEventAvailability (ID, eventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // FrontEnd/FrontEndCorba/bookEvent
       {
         String ID = in.read_string ();
         String customerID = in.read_string ();
         String eventID = in.read_string ();
         String eventType = in.read_string ();
         String $result = null;
         $result = this.bookEvent (ID, customerID, eventID, eventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // FrontEnd/FrontEndCorba/getBookingSchedule
       {
         String ID = in.read_string ();
         String customerID = in.read_string ();
         String $result = null;
         $result = this.getBookingSchedule (ID, customerID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 5:  // FrontEnd/FrontEndCorba/cancelEvent
       {
         String ID = in.read_string ();
         String customerID = in.read_string ();
         String eventID = in.read_string ();
         String eventType = in.read_string ();
         String $result = null;
         $result = this.cancelEvent (ID, customerID, eventID, eventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 6:  // FrontEnd/FrontEndCorba/swapEvent
       {
         String ID = in.read_string ();
         String customerID = in.read_string ();
         String newEventID = in.read_string ();
         String newEventType = in.read_string ();
         String oldEventID = in.read_string ();
         String oldEventType = in.read_string ();
         String $result = null;
         $result = this.swapEvent (ID, customerID, newEventID, newEventType, oldEventID, oldEventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:FrontEnd/FrontEndCorba:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public FrontEndCorba _this() 
  {
    return FrontEndCorbaHelper.narrow(
    super._this_object());
  }

  public FrontEndCorba _this(org.omg.CORBA.ORB orb) 
  {
    return FrontEndCorbaHelper.narrow(
    super._this_object(orb));
  }


} // class FrontEndCorbaPOA
