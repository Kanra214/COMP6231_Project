package serviceInterface;


/**
* serviceInterface/ServiceInterfaceOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从serviceInterface.idl
* 2019年7月17日 星期三 下午06时36分03秒 EDT
*/

public interface ServiceInterfaceOperations 
{
  String addEvent (String eventID, String eventType, int bookingCapacity);
  String removeEvent (String eventID, String eventType);
  String listEventAvailability (String eventType);
  String bookEvent (String customerID, String eventID, String eventType);
  String getBookingSchedule (String customerID);
  String cancelEvent (String customerID, String eventID, String eventType);
  String swapEvent (String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType);
} // interface ServiceInterfaceOperations
