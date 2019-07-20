package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    private static ClientManager clientManager;

    static {
        System.out.println("Starting CORBA on cmd.");
        try {
            Runtime.getRuntime().exec("cmd orbd -ORBInitialPort 1050 -ORBInitialHost localhost");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Started CORBA on cmd.");
    }

    public static void main(String[] args) throws Exception {
        boolean keepAlive = true;
        while (keepAlive) {
            try {
                String id = getUserInput("ID");
                clientManager = new ClientManager(args, id);
                boolean isManager = identifyID(id);
                showWelcomeMessage(clientManager.location);
                if (isManager)
                    keepAlive = loginAsManager(id);
                else keepAlive = loginAsCustomer(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean loginAsManager(String id) throws InterruptedException {
        boolean loggedIn = true;
        while (loggedIn) {
            showManagerChoices();
            try {
                String usersChoice = getUserInput("Choose");
                if (usersChoice.equals("1")) {
                    String eventID = getUserInput("Event ID");
                    String eventType = getUserInput("Event type");
                    int bookingCapacity = Integer.parseInt(getUserInput("Booking capacity"));
                    if (id.substring(0, 3).equalsIgnoreCase(eventID.substring(0, 3)))
                        clientManager.addEvent(eventID, eventType, bookingCapacity);
                } else if (usersChoice.equals("2")) {
                    String eventID = getUserInput("Event ID");
                    String eventType = getUserInput("Event type");
                    if (id.substring(0, 3).equalsIgnoreCase(eventID.substring(0, 3)))
                        clientManager.removeEvent(eventID, eventType);
                    else System.out.println("This operation is not allowed.");
                } else if (usersChoice.equals("3")) {
                    String eventType = getUserInput("Event type");
                    clientManager.listEventAvailability(eventType);
                } else if (usersChoice.equals("4")) {
                    String customerID = getUserInput("Customer ID");
                    String eventID = getUserInput("Event ID");
                    String eventType = getUserInput("Event type");
                    clientManager.bookEvent(customerID, eventID, eventType);
                } else if (usersChoice.equals("5")) {
                    String customerID = getUserInput("Customer ID");
                    clientManager.getBookingSchedule(customerID);
                } else if (usersChoice.equals("6")) {
                    String customerID = getUserInput("Customer ID");
                    String eventID = getUserInput("Event ID");
                    clientManager.cancelEvent(customerID, eventID);
                } else if (usersChoice.equals("7")) {
                    String customerID = getUserInput("Customer ID");
                    String newEventID = getUserInput("New event ID");
                    String newEventType = getUserInput("New event type");
                    String oldEventID = getUserInput("Old event ID");
                    String oldEventType = getUserInput("Old event type");
                    clientManager.swapEvent(customerID, newEventID, newEventType, oldEventID, oldEventType);
                } else if (usersChoice.equals("8")) {
                    loggedIn = false;
                } else if (usersChoice.equals("9")) {
                    return false;
                } else showManagerChoices();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return true;
    }

    private static boolean loginAsCustomer(String id) throws InterruptedException {
        boolean loggedIn = true;
        while (loggedIn) {
            showCustomerChoices();
            try {
                String usersChoice = getUserInput("Choose");
                if (usersChoice.equals("1")) {
                    //String customerID = getUserInput("Customer ID");
                    String eventID = getUserInput("Event ID");
                    String eventType = getUserInput("Event type");
                    clientManager.bookEvent(id, eventID, eventType);
                } else if (usersChoice.equals("2")) {
                    //String customerID = getUserInput("Customer ID");
                    clientManager.getBookingSchedule(id);
                } else if (usersChoice.equals("3")) {
                    //String customerID = getUserInput("Customer ID");
                    String eventID = getUserInput("Event ID");
                    clientManager.cancelEvent(id, eventID);
                } else if (usersChoice.equals("4")) {
                    //String customerID = getUserInput("Customer ID");
                    String newEventID = getUserInput("New event ID");
                    String newEventType = getUserInput("New event type");
                    String oldEventID = getUserInput("Old event ID");
                    String oldEventType = getUserInput("Old event type");
                    clientManager.swapEvent(id, newEventID, newEventType, oldEventID, oldEventType);
                } else if (usersChoice.equals("5")) {
                    loggedIn = false;
                } else if (usersChoice.equals("6")) {
                    return false;
                } else showCustomerChoices();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return true;
    }

    private static void showManagerChoices() throws InterruptedException {
        Thread.sleep(500);
        System.out.println();
        System.out.println("1. Add an event.");
        System.out.println("2. Remove an event");
        System.out.println("3. List event availability.");
        System.out.println("4. Book an event.");
        System.out.println("5. Get booking schedule.");
        System.out.println("6. Cancel an event.");
        System.out.println("7. Swap an event.");
        System.out.println("8. Sign out.");
        System.out.println("9. Exit.");
        System.out.println();
    }

    private static void showCustomerChoices() throws InterruptedException {
        Thread.sleep(500);
        System.out.println();
        System.out.println("1. Book an event.");
        System.out.println("2. Get booking schedule.");
        System.out.println("3. Cancel an event.");
        System.out.println("4. Swap an event.");
        System.out.println("5. Sign out.");
        System.out.println("6. Exit.");
        System.out.println();
    }

    private static boolean identifyID(String id) {
        String identifier = id.substring(3, 4);
        if (identifier.equals("M")) {
            return true;
        } else if (identifier.equals("C")) {
            return false;
        } else return false;
    }

    private static void showWelcomeMessage(String location) {
        System.out.println();
        System.out.println("****************************************");
        System.out.println("Welcome to " + location);
        System.out.println("****************************************");
    }

    public static String getUserInput(String field) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter " + field + ": ");
        return br.readLine();
    }
}
