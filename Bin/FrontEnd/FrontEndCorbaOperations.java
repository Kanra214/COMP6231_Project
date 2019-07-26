package FrontEnd;


/**
* FrontEnd/FrontEndCorbaOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从FrontEndCorba.idl
* 2019年7月25日 星期四 下午06时44分52秒 EDT
*/

public interface FrontEndCorbaOperations 
{
  String addEvent (String ID, String eventID, String eventType, int bookingCapacity);
  String removeEvent (String ID, String eventID, String eventType);
  String listEventAvailability (String ID, String eventType);
  String bookEvent (String ID, String customerID, String eventID, String eventType);
  String getBookingSchedule (String ID, String customerID);
  String cancelEvent (String ID, String customerID, String eventID, String eventType);
  String swapEvent (String ID, String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType);
} // interface FrontEndCorbaOperations
