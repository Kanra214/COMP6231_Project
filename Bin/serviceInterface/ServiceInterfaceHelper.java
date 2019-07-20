package serviceInterface;


/**
* serviceInterface/ServiceInterfaceHelper.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从serviceInterface.idl
* 2019年7月17日 星期三 下午06时36分03秒 EDT
*/

abstract public class ServiceInterfaceHelper
{
  private static String  _id = "IDL:serviceInterface/ServiceInterface:1.0";

  public static void insert (org.omg.CORBA.Any a, serviceInterface.ServiceInterface that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static serviceInterface.ServiceInterface extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (serviceInterface.ServiceInterfaceHelper.id (), "ServiceInterface");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static serviceInterface.ServiceInterface read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_ServiceInterfaceStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, serviceInterface.ServiceInterface value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static serviceInterface.ServiceInterface narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof serviceInterface.ServiceInterface)
      return (serviceInterface.ServiceInterface)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      serviceInterface._ServiceInterfaceStub stub = new serviceInterface._ServiceInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static serviceInterface.ServiceInterface unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof serviceInterface.ServiceInterface)
      return (serviceInterface.ServiceInterface)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      serviceInterface._ServiceInterfaceStub stub = new serviceInterface._ServiceInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
