package FrontEnd;

/**
* FrontEnd/FrontEndCorbaHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从FrontEndCorba.idl
* 2019年7月25日 星期四 下午06时44分52秒 EDT
*/

public final class FrontEndCorbaHolder implements org.omg.CORBA.portable.Streamable
{
  public FrontEnd.FrontEndCorba value = null;

  public FrontEndCorbaHolder ()
  {
  }

  public FrontEndCorbaHolder (FrontEnd.FrontEndCorba initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FrontEnd.FrontEndCorbaHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FrontEnd.FrontEndCorbaHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FrontEnd.FrontEndCorbaHelper.type ();
  }

}
