package Test;
import Client.Client;
import Client.EventManager;
import Client.Customer;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;


public class TestCase {
    static ArrayList<Client> customerClient = new ArrayList<>();
    Customer customer1 = new Customer(new String[]{"OTWC1111", "OTW", "Customer"});

    @Before
    public void before () {
        EventManager eventManager1 = new EventManager(new String[]{"TORM1111", "TOR", "Manager"});
        EventManager eventManager2 = new EventManager(new String[]{"OTWM1111", "OTW", "Manager"});

        eventManager1.addEvent("TORA100519", "Conferences",2);
        eventManager1.addEvent("TORA100619", "Conferences",2);
        eventManager1.addEvent("TORA100719", "Conferences",1);
        eventManager2.addEvent("OTWE100519", "Seminars",2);
        customer1.bookEvent("OTWC1111","OTWE100519", "Seminars");


        customerClient.add(new Customer(new String[]{"TORC1111", "TOR", "Customer"}));
        customerClient.add(new Customer(new String[]{"TORC1112", "TOR", "Customer"}));
        customerClient.add(new Customer(new String[]{"TORC1113", "TOR", "Customer"}));


    }
    @Test
    public void testCase1() {
        ArrayList<Thread> threadList = new ArrayList<>();

        customerClient.stream().forEach(studentClient -> {
            threadList.add(new Thread(() -> {
                studentClient.bookEvent(studentClient.getClientID(), "TORA100519", "Conferences");
            }));
        });

        threadList.forEach(t -> t.start());
    }
    @Test
    public void testCase2() {
        new Thread(() -> customerClient.get(0).bookEvent(customerClient.get(0).getClientID(), "TORA100619", "Conferences")).start();
        new Thread(() -> customerClient.get(1).bookEvent(customerClient.get(1).getClientID(), "TORA100619", "Conferences")).start();
        new Thread(() -> customer1.swapEvent("OTWC1111", "TORA100619", "Conferences", "OTWE100519","Seminars")).start();

    }






}
