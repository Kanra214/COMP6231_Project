package Client;


import MyApp.Handler;
import MyApp.HandlerHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import static Client.Requests.*;
import static java.lang.Character.isDigit;


public class ClientMain {
    private static InputStreamReader is;
    private static BufferedReader br;
    private static Handler handler;
    private static String id;
    private static String[] argss;
    private static boolean test = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        argss = args;
        is = new InputStreamReader(System.in);
        br = new BufferedReader(is);
        if(test) {
            doTest();
        }


        printMenu();
        int choice = forceCorrectChoice(2);
        while (true) {
            switch (choice) {
                case 0:
                    System.out.println("Bye.");
                    System.exit(0);
                case 1:
                    login();
                    break;
                case 2:
                    signup();
                    break;
                default:
                    System.out.println("sth is wrong");

            }
            printMenu();
            choice = forceCorrectChoice(2);

        }
    }

        //loop util user inputs correct format id



    private static int forceCorrectChoice(int len) throws IOException {
        while (true) {
            System.out.println("Please select a choice [0, " + len + "]");
            try {
                int input = Integer.parseInt(br.readLine());
                if (input <= len && input >= 0) {
                    return input;
                } else {
                    System.out.println("Wrong input, try again please");
                    continue;

                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again please");
                continue;
            }

        }
    }

    private static boolean checkId(String tid) {
        if (tid == null) {
            return false;
        }
        if (tid.length() == 8) {
            String branch = tid.substring(0, 3);
            if (branch.equals("TOR") || branch.equals("MTL") || branch.equals("OTW")) {
                if (tid.charAt(3) == 'C' || tid.charAt(3) == 'M') {
                    for (char c : tid.substring(4, 8).toCharArray()) {
                        if (!isDigit(c)) {
                            return false;
                        }
                    }
                    return true;

                }
            }
        }
        return false;

    }
    private static boolean checkEventId(String eventId) {
        if (eventId == null) {
            return false;
        }
        if (eventId.length() == 10) {
            String branch = eventId.substring(0, 3);
            if (branch.equals("TOR") || branch.equals("MTL") || branch.equals("OTW")) {
                if (eventId.charAt(3) == 'M' || eventId.charAt(3) == 'A' || eventId.charAt(3) == 'E') {
                    for (char c : eventId.substring(4, 10).toCharArray()) {
                        if (!isDigit(c)) {
                            return false;
                        }
                    }
                    return true;

                }
            }
        }
        return false;

    }
    private static boolean checkEventType(String inputType) {
        if (inputType == null) {
            return false;
        }
        for(String s : Event.EVENTTYPES){
            if(s.equals(inputType)){
                return true;
            }
        }
        return false;

    }

    private static void printMenu() {
        System.out.println("Welcome to DEMS.");
        System.out.println("0. Exit");
        System.out.println("1. Log in");
        System.out.println("2. Sign up");

    }

    private static String forceCorrectFormat(int option) {
        String userInput;
        try {
            userInput = br.readLine().trim();
        } catch (IOException e) {
            userInput = null;
        }
        if(option == 1){//check id
            while (!checkId(userInput)) {
                System.out.println("Wrong id format, try again please.");
                try {
                    userInput = br.readLine().trim();
                } catch (IOException e) {
                    userInput = null;
                }
            }
        }
        else if(option == 2){
            while (!checkEventId(userInput)) {
                System.out.println("Wrong event id format, try again please.");
                try {
                    userInput = br.readLine().trim();
                } catch (IOException e) {
                    userInput = null;
                }
            }
        }
        else if(option == 3){

            while (!checkEventType(userInput)) {
                System.out.println("Wrong event type format, try again please.");
                try {
                    userInput = br.readLine().trim();
                } catch (IOException e) {
                    userInput = null;
                }
            }
        }

        return userInput;
    }



    private static Handler getRelatedHandler(String tempId) {
        Branch branch = Branch.getBranchFromString(tempId.substring(0, 3));
        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", branch.getOrbPort());
            props.put("org.omg.CORBA.ORBInitialHost", "localhost");
            ORB orb = ORB.init(argss, props);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            Handler h = HandlerHelper.narrow(ncRef.resolve_str(branch.name()));
            return h;
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        }
        return null;
    }



    private static void login () throws IOException {
        System.out.println("Please enter client id");
        id = forceCorrectFormat(1);
        handler = getRelatedHandler(id);
        MyJsonObject res = MyJsonObject.stringToObject(handler.login(id));
        System.out.println(res.getResponse());
        if(res.isApproved()){
            operate();
        }

    }






    private static void printOperations(Requests[] list){
        int option = 1;
        System.out.println("0. Go back");
        for (Requests r : list){
            System.out.println(option + ". " + r.name());
            option++;
        }
    }


    private static void operate() throws IOException {
        String listString = handler.getRequestList(id);
        String[] temp = listString.split(";");
        Requests[] requestList = new Requests[temp.length];
        for(int i = 0; i < temp.length; i++){
            requestList[i] = Requests.getRequest(temp[i]);
        }

        printOperations(requestList);

        int choice = forceCorrectChoice(requestList.length);
        while(choice != 0) {

            switch (choice) {
                case 1:
                    bookEvent();
                    break;
                case 2:
                    cancelEvent();
                    break;
                case 3:
                    swapEvent();
                    break;
                case 4:
                    getBookingSchedule();
                    break;
                case 5:
                    addEvent();
                    break;
                case 6:
                    removeEvent();
                    break;
                case 7:
                    listEventAvailability();
                    break;

            }
            printOperations(requestList);
            choice = forceCorrectChoice(requestList.length);

        }





    }
    private static void addEvent() throws IOException {
        MyJsonObject res = MyJsonObject.stringToObject(handler.authenEntry(id, AddEvent.name()));
        String eventId, eventTypeString;
        int capacity;
        System.out.println(res.getResponse());
        if(res.isApproved()){//manager
            eventId = forceCorrectFormat(2);
            eventTypeString = forceCorrectFormat(3);
            capacity = forceCorrectChoice(500);
            res = MyJsonObject.stringToObject(handler.addEvent(id, eventId, eventTypeString, capacity));
            System.out.println(res.getResponse());

        }


    }

    private static void removeEvent() throws IOException {
        MyJsonObject res = MyJsonObject.stringToObject(handler.authenEntry(id, RemoveEvent.name()));
        System.out.println(res.getResponse());
        if(res.isApproved()){//manager
            String eventId = forceCorrectFormat(2);
            String eventTypeString = forceCorrectFormat(3);
            res = MyJsonObject.stringToObject(handler.removeEvent(id, eventId, eventTypeString));
            System.out.println(res.getResponse());

        }


    }
    private static void listEventAvailability(){
        MyJsonObject res = MyJsonObject.stringToObject(handler.authenEntry(id, ListEventAvailability.name()));
        System.out.println(res.getResponse());
        if(res.isApproved()){//manager
            String eventTypeString = forceCorrectFormat(3);
            res =  MyJsonObject.stringToObject(handler.listEventAvailability(id, eventTypeString));
            System.out.println(res.getResponse());

        }

    }

    private static void getBookingSchedule(){
        MyJsonObject res =  MyJsonObject.stringToObject(handler.authenEntry(id, GetBookingSchedule.name()));
        System.out.println(res.getResponse());
        if(res.isApproved()) {//manager
            String tempId = forceCorrectFormat(1);
            res =  MyJsonObject.stringToObject(handler.getBookingSchedule(tempId,id));
            System.out.println(res.getResponse());
        }
        else{//customer
            res =  MyJsonObject.stringToObject(handler.getBookingSchedule(id, ""));
            System.out.println(res.getResponse());

        }

    }

    private static void cancelEvent(){
        MyJsonObject res =  MyJsonObject.stringToObject(handler.authenEntry(id, CancelEvent.name()));
        String eventId, eventTypeString;
        System.out.println(res.getResponse());
        if(res.isApproved()){//manager
            String tempId = forceCorrectFormat(1);
            eventId = forceCorrectFormat(2);
            eventTypeString = forceCorrectFormat(3);
            res =  MyJsonObject.stringToObject(handler.cancelEvent(tempId, id, eventId, eventTypeString));
            System.out.println(res.getResponse());


        }
        else{//customer
            eventId = forceCorrectFormat(2);
            eventTypeString = forceCorrectFormat(3);
            res =  MyJsonObject.stringToObject(handler.cancelEvent(id, "", eventId, eventTypeString));
            System.out.println(res.getResponse());

        }


    }

    private static void bookEvent(){
        MyJsonObject res =  MyJsonObject.stringToObject(handler.authenEntry(id, BookEvent.name()));
        String eventId, eventTypeString;
        System.out.println(res.getResponse());
        if(res.isApproved()){//manager
            String tempId = forceCorrectFormat(1);
            eventId = forceCorrectFormat(2);
            eventTypeString = forceCorrectFormat(3);
            res =  MyJsonObject.stringToObject(handler.bookEvent(tempId, id, eventId, eventTypeString));
            System.out.println(res.getResponse());


        }
        else{//customer
            eventId = forceCorrectFormat(2);
            eventTypeString = forceCorrectFormat(3);
            res =  MyJsonObject.stringToObject(handler.bookEvent(id, "", eventId, eventTypeString));
            System.out.println(res.getResponse());

        }

    }
    private static void signup() {
        System.out.println("Please enter a new id");
        String tempId = forceCorrectFormat(1);
        Handler tempHandler = getRelatedHandler(tempId);
        MyJsonObject res =  MyJsonObject.stringToObject(tempHandler.signup(tempId));
        System.out.println(res.getResponse());

    }
    private static void swapEvent(){
        MyJsonObject res =  MyJsonObject.stringToObject(handler.authenEntry(id, SwapEvent.name()));
        String neid, net, oeid, oet;
        System.out.println(res.getResponse());
        if(res.isApproved()){//manager
            String tempId = forceCorrectFormat(1);
            neid = forceCorrectFormat(2);
            net = forceCorrectFormat(3);
            oeid = forceCorrectFormat(2);
            oet = forceCorrectFormat(3);
            res =  MyJsonObject.stringToObject(handler.swapEvent(tempId, id, neid, net, oeid, oet));
            System.out.println(res.getResponse());


        }
        else{//customer
            neid = forceCorrectFormat(2);
            net = forceCorrectFormat(3);
            oeid = forceCorrectFormat(2);
            oet = forceCorrectFormat(3);
            res =  MyJsonObject.stringToObject(handler.swapEvent(id, "", neid, net, oeid, oet));
            System.out.println(res.getResponse());

        }
    }

    private static void doTest() throws InterruptedException {
        int i = 1;

        //1
        System.out.println("-------------------test" + i++ + "-------------------");
        String tempId = "TORM3456";
        Handler tempHandler = getRelatedHandler(tempId);
        tempHandler.signup(tempId);
        getResp(tempHandler.addEvent(tempId, "TORE080619", "Conference", 2));
        getResp(tempHandler.addEvent(tempId, "TORE110619", "Seminar", 1));


        //2
        System.out.println("-------------------test" + i++ + "-------------------");
        tempId = "MTLM9000";
        tempHandler = getRelatedHandler(tempId);
        tempHandler.signup(tempId);
        getResp(tempHandler.addEvent(tempId, "MTLA090619", "Conference", 2));
        getResp(tempHandler.addEvent(tempId, "MTLA080619", "TradeShow", 1));

        //3
        System.out.println("-------------------test" + i++ + "-------------------");
        tempId = "OTWM9000";
        tempHandler = getRelatedHandler(tempId);
        tempHandler.signup(tempId);
        getResp(tempHandler.addEvent(tempId, "OTWA190619", "Conference", 1));
        getResp(tempHandler.addEvent(tempId, "OTWA250619", "Seminar", 1));
        //4
        System.out.println("-------------------test" + i++ + "-------------------");
        getResp(tempHandler.listEventAvailability(tempId, "Conference"));
        getResp(tempHandler.listEventAvailability(tempId, "Seminar"));
        getResp(tempHandler.listEventAvailability(tempId, "TradeShow"));


        //5
        System.out.println("-------------------test" + i++ + "-------------------");
        tempId = "OTWC1234";
        tempHandler = getRelatedHandler(tempId);
        tempHandler.signup(tempId);
        getResp(tempHandler.bookEvent(tempId,"", "TORE080619", "Conference"));
        getResp(tempHandler.bookEvent(tempId,"", "TORE110619", "Seminar"));
        getResp(tempHandler.bookEvent(tempId,"", "MTLA090619", "Conference"));
        getResp(tempHandler.bookEvent(tempId,"", "OTWA190619", "Conference"));
        getResp(tempHandler.getBookingSchedule(tempId,""));
        //6
        System.out.println("-------------------test" + i++ + "-------------------");
        getResp(tempHandler.swapEvent(tempId,"", "MTLA080619", "TradeShow", "OTWA190619", "Conference"));
        getResp(tempHandler.swapEvent(tempId,"", "OTWA250619", "Seminar", "TORE080619", "Conference"));
        getResp(tempHandler.getBookingSchedule(tempId,""));
        //7
        System.out.println("-------------------test" + i++ + "-------------------");
        tempId = "TORM3456";
        String clId = "TORC1234";
        tempHandler = getRelatedHandler(clId);
        tempHandler.signup(clId);
        tempHandler = getRelatedHandler(tempId);

        getResp(tempHandler.bookEvent(clId,tempId, "MTLA080619", "TradeShow"));
        getResp(tempHandler.swapEvent(clId,tempId, "OTWA250619", "Seminar", "TORE080619", "Conference"));
        getResp(tempHandler.swapEvent(clId,tempId, "TORE080619", "Seminar", "MTLA090619", "Conference"));
        getResp(tempHandler.getBookingSchedule(clId,tempId));


        //8
        System.out.println("-------------------test" + i++ + "-------------------");
        tempId = "MTLM9000";
        tempHandler = getRelatedHandler(tempId);
        getResp(tempHandler.listEventAvailability(tempId, "Conference"));
        getResp(tempHandler.listEventAvailability(tempId, "Seminar"));
        getResp(tempHandler.listEventAvailability(tempId, "TradeShow"));


        //9
        System.out.println("-------------------test" + i++ + "-------------------");
        Handler torHandler = getRelatedHandler("TORC1234");
        Handler otwHandler = getRelatedHandler("OTWC1234");
        Runnable r1 = new Task(torHandler, "TORC1234", "OTWA190619", "Conference", "MTLA080619", "TradeShow", 1);
        Runnable r2 = new Task(otwHandler, "OTWC1234", "MTLA080619", "TradeShow", "OTWA190619", "Conference", 2);
        Runnable r3 = new Task(torHandler, "TORC1234", "MTLA090619", "Conference", "MTLA080619", "TradeShow", 3);
        Runnable r4 = new Task(otwHandler, "OTWC1234", "MTLA080619", "TradeShow", "OTWA190619", "Conference", 4);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Thread t3 = new Thread(r3);
        Thread t4 = new Thread(r4);
        t3.start();
        t4.start();
        t3.join();
        t4.join();
        tempId = "MTLM9000";
        tempHandler = getRelatedHandler(tempId);
        getResp(tempHandler.listEventAvailability(tempId, "Conference"));
        getResp(tempHandler.listEventAvailability(tempId, "Seminar"));
        getResp(tempHandler.listEventAvailability(tempId, "TradeShow"));






    }
    private static void getResp(String s){
        System.out.println(MyJsonObject.stringToObject(s).getResponse());
    }
    static class Task implements Runnable {
        String id;
        String nid;
        String net;
        String oid;
        String oet;
        Handler h;
        int taskNum;
        public Task(Handler h, String id, String nid, String net, String oid, String oet, int taskNum){
            this.id = id;
            this.nid = nid;
            this.net = net;
            this.oid = oid;
            this.oet = oet;
            this.h = h;
            this.taskNum = taskNum;
        }

        @Override
        public void run() {
            System.out.println(taskNum + ": " + MyJsonObject.stringToObject(h.swapEvent(id, "", nid, net, oid, oet)).getResponse());



        }
    }

}
