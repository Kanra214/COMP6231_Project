package Client;

import java.util.Scanner;
public class ClientDriver {
    private Scanner scanner;

    public ClientDriver() {
        scanner = new Scanner(System.in);
        this.init();
    }

    private void init() {
        String[] info1 = {"TORM3456", "TOR", "Manager"};
        String[] info2 = {"MTLM9000", "MTL", "Manager"};
        String[] info3 = {"TORC1234", "TOR", "Customer"};
        String[] info4 = {"OTWM9000", "OTW", "Manager"};
        String[] info6 = {"OTWM6785", "OTW", "Manager"};
        String[] info5 = {"OTWC1234", "OTW", "Customer"};


        EventManager eventManager1 = new EventManager(info1);
        EventManager eventManager2 = new EventManager(info2);
        EventManager eventManager3 = new EventManager(info4);
        EventManager eventManager4 = new EventManager(info6);

        Customer customer1 = new Customer(info3);
        Customer customer2 = new Customer(info5);
//        eventManager1.addEvent("TORA110519", "Conferences",1);
//        eventManager2.addEvent("OTWE110519", "Conferences",1);
//        eventManager1.addEvent("TORA130519", "Conferences",1);
//        customer1.bookEvent("TORC1234", "TORA110519","Conferences");
//        customer1.bookEvent("TORC1111", "TORA110519","Conferences");
//        customer1.bookEvent("TORC1111", "TORA120519","Conferences");
//        customer1.bookEvent("TORC1111", "TORA130519","Conferences");
//        eventManager2.addEvent("OTWE100519", "Seminars",2);
//        eventManager1.swapEvent("TORC1234", "OTWE110519", "Conferences", "TORA110519", "Conferences");
//        eventManager2.addEvent("OTWE120519", "Conferences",2);
//        eventManager3.addEvent("MTLA110519", "TradeShows", 2);
//        eventManager3.addEvent("MTLM240519", "Seminars", 2);
//
//        customer1.bookEvent("TORC1234", "OTWE120519", "Conferences");
//        customer1.bookEvent("TORC1234", "MTLA110519", "TradeShows");
//        customer1.bookEvent("TORC1234", "MTLM240519", "Seminars");
//        customer1.swapEvent("TORC1234", "OTWE110519", "Conferences", "TORA110519", "Conferences");
//        customer1.swapEvent("TORC1234", "OTWE110519", "Conferences", "MTLA110519", "TradeShows");
//        customer1.swapEvent("TORC1234", "OTWE110519", "Conferences", "TORA110519", "Conferences");
//        customer1.getBookingSchedule("TORC1111");




//        eventManager2.removeEvent("OTWE100519","Seminars");
//        customer.cancelEvent("TORC1111", "OTWE100519", "Seminars");
//        customer.getBookingSchedule("TORC1111");
//        eventManager.listEventAvailability("Conferences");
//        eventManager.removeEvent("TORA100519","Conferences");
    }


    public void run() {
        this.mainMenu();
    }

    private void menu() {
        System.out.println();
        System.out.println("input your ID: ");
        IDAnalyse IDAnalyse = new IDAnalyse(this.scanner.nextLine().toUpperCase());
        while (!IDAnalyse.checkID()) {
            System.out.println("input your ID");
            IDAnalyse.setID(this.scanner.nextLine().toUpperCase());
        }
        this.optionPanel(IDAnalyse.analyseID());
    }

    private void optionPanel(String[] analyseID) {

        if (analyseID[2].equals("Customer")) {
                this.CustomerRun(analyseID);
        } else if (analyseID[2].equals("Manager")) {
            this.ManagerRun(analyseID);
        } else {
            System.out.println("Nobody");
        }
    }

    private void ManagerPanel () {
        System.out.println();
        System.out.println("1. addEvent");
        System.out.println("2. removeEvent");
        System.out.println("3. listEventAvailability");
        System.out.println("4. bookEvent");
        System.out.println("5. getBookingSchedule");
        System.out.println("6. cancelEvent");
        System.out.println("7. swapEvent");
        System.out.println("8. quit");
    }

    private void CutomerPanel () {
        System.out.println();
        System.out.println("1. bookEvent");
        System.out.println("2. getBookingSchedule");
        System.out.println("3. cancelEvent");
        System.out.println("4. swapEvent");
        System.out.println("5. quit");
    }

    private void CustomerRun(String[] analyseID) {
        while (true) {
            this.CutomerPanel();
            Customer customer = new Customer(analyseID);
            String ops = scanner.nextLine();
            switch (ops) {
                case "1":
                    System.out.println(this.bookEvent(customer));
                    break;
                case "2":
                    System.out.println(this.getBookingSchedule(customer));
                    break;
                case "3":
                    System.out.println(this.cancelEvent(customer));
                    break;
                case "4":
                    System.out.println(this.swapEvent(customer));
                    break;
                case "5":
                    return;
                default:
                    System.out.println("no selected option");

            }
        }
    }

    private void ManagerRun(String[] analyseID) {
        while (true) {
            this.ManagerPanel();
            EventManager eventManager = new EventManager(analyseID);
            String ops = scanner.nextLine();
            switch (ops) {
                case "1":
                    System.out.println(this.addEvent(eventManager));
                    break;
                case "2":
                    System.out.println(this.removeEvent(eventManager));
                    break;
                case "3":
                    System.out.println(this.listCourseAvailability(eventManager));
                    break;
                case "4":
                    System.out.println(this.bookEvent(eventManager));
                    break;
                case "5":
                    System.out.println(this.getBookingSchedule(eventManager));
                    break;
                case "6":
                    System.out.println(this.cancelEvent(eventManager));
                    break;
                case "7":
                    System.out.println(this.swapEvent(eventManager));
                    break;
                case "8":
                    return;
                default:
                    System.out.println("no selected option");
            }
        }
    }

    private String bookEvent(Client client) {
        System.out.println("input your parameter");
        String cutomerID = getCutomerID(client);
        System.out.println("eventID: ");
        String enentID = this.scanner.nextLine();
        System.out.println();
        System.out.println("eventType(Conferences, Seminars, Trade Shows): ");
        String semester = this.scanner.nextLine();
        System.out.println();
        return client.bookEvent(client.clientID,cutomerID, enentID, semester);
    }

    private String getBookingSchedule(Client client) {
        System.out.println("input your parameter");
        String customerID = getCutomerID(client);
        return client.getBookingSchedule(client.clientID,customerID);
    }

    private String cancelEvent(Client client) {
        System.out.println("input your parameter");
        String customerID = getCutomerID(client);
        System.out.println("eventID: ");
        String eventID = this.scanner.nextLine();
        System.out.println();
        System.out.println("eventType :");
        String eventType = this.scanner.nextLine();
        return client.cancelEvent(client.clientID,customerID, eventID,eventType);
    }

    private String getCutomerID(Client client) {
        String customerID;
        if (!client.getIdentify().equals("Customer")){
            System.out.println("CustomerID: ");
            customerID = this.scanner.nextLine();
            System.out.println();
        }  else {
            customerID = client.getClientID();
        }
        return customerID;
    }

    private String addEvent(EventManager eventManager) {
        System.out.println("input your parameter");
        System.out.println("eventID: ");
        String eventID = this.scanner.nextLine();
        System.out.println();
        System.out.println("eventType(Conferences, Seminars, Trade Shows): ");
        String eventType = this.scanner.nextLine();
        System.out.println();
        System.out.println("Capacity: ");
        int bookingCapacity = this.scanner.nextInt();
        return eventManager.addEvent(eventManager.clientID, eventID, eventType,bookingCapacity);
    }

    private String removeEvent(EventManager eventManager) {
        System.out.println("input your parameter");
        System.out.println("eventID: ");
        String eventID = this.scanner.nextLine();
        System.out.println();
        System.out.println("eventType(Conferences, Seminars, Trade Shows): ");
        String eventType = this.scanner.nextLine();
        System.out.println();
        return eventManager.removeEvent(eventManager.clientID,eventID, eventType);
    }

    private String listCourseAvailability(EventManager eventManager) {
        System.out.println("input your parameter");
        System.out.println("eventType(Conferences, Seminars, Trade Shows): ");
        String eventType = this.scanner.nextLine();
        System.out.println();
        return eventManager.listEventAvailability(eventManager.clientID, eventType);
    }
    private String swapEvent(Client client) {
        System.out.println("input your parameter");
        String customerID = getCutomerID(client);
        System.out.println("newEventID: ");
        String newEventID = this.scanner.nextLine();
        System.out.println();
        System.out.println("newEventType: ");
        String newEventType = this.scanner.nextLine();
        System.out.println();
        System.out.println("oldEventID: ");
        String oldEventID = this.scanner.nextLine();
        System.out.println();
        System.out.println("oldEventType: ");
        String oldEventType = this.scanner.nextLine();
        System.out.println();
        return client.swapEvent(client.clientID, customerID, newEventID, newEventType, oldEventID, oldEventType);
    }

    private void mainMenu() {
        while (true) {
            System.out.println("Operations: ");
            System.out.println("            1. Sign in");
            System.out.println("            2. test multiple clients");
            System.out.println("            3. quit");
            String ops = this.scanner.nextLine();
            switch (ops) {
                case "1":
                    this.menu();
                    break;
                case "2":
                    System.out.println(2);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("no match");
            }
        }
    }

    public static void main(String args[]) {
        ClientDriver clientDriver = new ClientDriver();
        clientDriver.run();
    }
}
