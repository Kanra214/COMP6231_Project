package serviceInterface;

/**
* serviceInterface/ServiceInterfaceHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从serviceInterface.idl
* 2019年7月17日 星期三 下午06时36分03秒 EDT
*/

public final class ServiceInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public serviceInterface.ServiceInterface value = null;

  public ServiceInterfaceHolder ()
  {
  }

  public ServiceInterfaceHolder (serviceInterface.ServiceInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = serviceInterface.ServiceInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    serviceInterface.ServiceInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return serviceInterface.ServiceInterfaceHelper.type ();
  }

}
