package FrontEnd;


/**
* FrontEnd/FrontEndCorbaHelper.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从FrontEndCorba.idl
* 2019年7月20日 星期六 上午11时43分28秒 EDT
*/

abstract public class FrontEndCorbaHelper
{
  private static String  _id = "IDL:FrontEnd/FrontEndCorba:1.0";

  public static void insert (org.omg.CORBA.Any a, FrontEnd.FrontEndCorba that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static FrontEnd.FrontEndCorba extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (FrontEnd.FrontEndCorbaHelper.id (), "FrontEndCorba");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static FrontEnd.FrontEndCorba read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_FrontEndCorbaStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, FrontEnd.FrontEndCorba value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static FrontEnd.FrontEndCorba narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof FrontEnd.FrontEndCorba)
      return (FrontEnd.FrontEndCorba)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      FrontEnd._FrontEndCorbaStub stub = new FrontEnd._FrontEndCorbaStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static FrontEnd.FrontEndCorba unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof FrontEnd.FrontEndCorba)
      return (FrontEnd.FrontEndCorba)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      FrontEnd._FrontEndCorbaStub stub = new FrontEnd._FrontEndCorbaStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
