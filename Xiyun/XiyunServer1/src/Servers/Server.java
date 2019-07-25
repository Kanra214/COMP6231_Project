package Servers;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

import static Common.Requests.*;


public class Server {
    private Branch branch;
    private EventRecords eventRecords;
    private String workingDir;
    private Log log;
    private DatagramSocket socketReply;
    private DatagramSocket socketSend;
    private HashMap<String, Client> clientList;

    public Server(Branch b){
        branch = b;
        workingDir = System.getProperty("user.dir") + "/" + branch;
        File dirFile = new File(workingDir);
        dirFile.mkdirs();

        log = new Log(workingDir + "/Server.txt");
        clientList = new HashMap<>();
        eventRecords = new EventRecords();





    }
    public void start(){
            System.out.println("Start");
            MyJsonObject o = new MyJsonObject();
            o.setRequest(StartServer);
            logAction(new Date(), o, false, true);


            try {
                socketReply = new DatagramSocket(branch.getUdpReplyPort());
                socketSend = new DatagramSocket(branch.getUdpSendPort());
                byte[] buffer = new byte[1024];
                while(true){
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                    socketReply.receive(request);
//                    Branch from = Branch.getBranchFromPort(request.getPort());
                    System.out.println("Receive request from branch " + Branch.getBranchFromPort(request.getPort()).name());
                    Date date = new Date();
                    MyJsonObject requestObj = parsePacket(request);
                    requestObj.setTargetServer(null);
                    requestObj.setSourceServer(Branch.getBranchFromPort(request.getPort()));
                    logAction(date, requestObj, false,  true);//server log on receiving

                    MyJsonObject replyObj = parseRequest(requestObj);
                    replyObj.setSourceServer(Branch.getBranchFromPort(request.getPort()));
                    replyObj.setTargetServer(null);
                    System.out.println("Replying request " + requestObj.getRequest().name() + " from branch " + Branch.getBranchFromPort(request.getPort()).name());
                    if(replyObj.isApproved()) {
                        System.out.println("Request is approved");
                    }
                    else{
                        System.out.println("Request is denied");
                    }
                    send(replyObj);
                    }

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    private MyJsonObject parseRequest(MyJsonObject obj){
        System.out.println("Parsing request " + obj.getRequest().name());
        switch(obj.getRequest()){
            case BookEvent:
                return bookEvent(obj.getClientId(), obj.getManagerId(), obj.getEventId(), obj.getEventType());
            case CancelEvent:
                return cancelEvent(obj.getClientId(), obj.getManagerId(), obj.getEventId(), obj.getEventType());
            case GetBookingSchedule:
                return getBookingSchedule(obj.getClientId(), obj.getManagerId());
            case AddEvent:
                return addEvent(obj.getClientId(), obj.getEventId(), obj.getEventType(), obj.getCapacity());
            case RemoveEvent:
                return removeEvent(obj.getClientId(), obj.getEventId(), obj.getEventType());
            case GetEventList:
                return getEventList(obj);
            case ModifyEventList:
                return modifyEventRecord(obj);
            case ModifyClientList:
                return modifyClientList(obj);



        }
        return null;

    }
    private MyJsonObject parsePacket(DatagramPacket pa) {//reply or request
        String msg = new String(pa.getData(), 0, pa.getLength());
        HashMap<String, String> param = MyJsonObject.stringToHashMap(msg);
        MyJsonObject o = new MyJsonObject();
        o.setFromHashMap(param);
        return o;
    }

    private MyJsonObject modifyClientList(MyJsonObject o){
        if(o.getOriginalRequest() == RemoveEvent){
            synchronized (clientList){
                for(Client c : clientList.values()){
                    c.removeEvent(o.getEventId(), o.getEventType());

                }
            }
        }
        o.setResponse("Event is removed from client's eventList");
        o.setApproved(true);
        return o;
    }



    private MyJsonObject modifyEventRecord(MyJsonObject obj){
        System.out.println("Original Request is " + obj.getOriginalRequest());
        String rescode;
        switch(obj.getOriginalRequest()) {
            case BookEvent:
                rescode = eventRecords.addClient(obj.getEventId() , obj.getEventType());
                obj.setApproved((rescode == "200"));
                obj.setResponse(rescode);
                break;
            case CancelEvent:
                rescode = eventRecords.removeClient(obj.getEventId(), obj.getEventType());
                obj.setApproved(rescode == "200");
                obj.setResponse(rescode);
                break;


        }
        return obj;
    }







    public void createClient(String id){
//    public MyJsonObject createClient(String id){
//        Date date = new Date();
//        MyJsonObject o = new MyJsonObject();
//        o.setRequest(Requests.CreateClient);
//        o.setClientId(id);
        Client c;
        synchronized (clientList) {

            if (!clientList.containsKey(id)) {
                c = new Client(id);
                clientList.put(id, c);
//                o.setResponse("New client is created, id = " + id + ".");
//                o.setApproved(true);


            }
//            else {
//                c = clientList.get(id);
//                o.setResponse("Client( id = " + id + ") has been used, please try another id.");
//                o.setApproved(false);
//
//
//            }
//            logAction(date, o, false, true);
//
//            return o;
        }

    }

//    public MyJsonObject login(String id){
//        MyJsonObject o = new MyJsonObject();
//        synchronized (clientList) {
//            if (!clientList.containsKey(id)) {
//                o.setResponse("Unknown id. You can either try another id or create a new id.");
//                o.setApproved(false);
//            } else {
//
//                o.setResponse("Id recognized, please go ahead to perform operations.");
//                o.setApproved(true);
//
//
//            }
//            return o;
//        }
//    }


//    public Requests[] getRequestList(String id){
//        Requests[] re;
//        if(isManager(id)) {
//            re = new Requests[7];
//            re[0] = BookEvent;
//            re[1] = CancelEvent;
//            re[2] = SwapEvent;
//            re[3] = GetBookingSchedule;
//            re[4] = AddEvent;
//            re[5] = RemoveEvent;
//            re[6] = ListEventAvailability;
//        }else {
//            re = new Requests[4];
//            re[0] = BookEvent;
//            re[1] = CancelEvent;
//            re[2] = SwapEvent;
//            re[3] = GetBookingSchedule;
//        }
//
//
//        return re;
//    }





    private void logAction(Date date, MyJsonObject obj, boolean clientCopy, boolean serverCopy){
        System.out.println("Logging action");
        String input = "Date:" + date.toString() + ";";
        HashMap<String, String> param = obj.toHashMap();

        if(param != null) {
            for (String k : param.keySet()) {
                input += k + ":" + param.get(k) + ";";
            }
        }

        if(clientCopy){
            if(obj.getClientId() != null){
                if(clientList.containsKey(obj.getClientId())){
                    clientList.get(obj.getClientId()).log.writeToFile(input);
                }
            }
            if(obj.getManagerId() != null){
                if(clientList.containsKey(obj.getManagerId())){
                    if(!obj.getManagerId().equals(obj.getClientId())) {
                        clientList.get(obj.getManagerId()).log.writeToFile(input);//if clientId = managerId, then manager will have only one copy
                    }
                }
            }
        }

        if(serverCopy){log.writeToFile(input);};

        //






    }


//    public MyJsonObject authenEntry(String id, Requests req){
//        MyJsonObject o = new MyJsonObject();
//        if(isManager(id)){
//            o.setApproved(true);
//            switch(req){
//                case BookEvent:
//                    o.setResponse("Please enter the id of which client you want him to be managed, event id and event type");
//                    break;
//                case CancelEvent:
//                    o.setResponse("Please enter the id of which client you want him to be managed, event id and event type");
//                    break;
//                case SwapEvent:
//                    o.setResponse("Please enter the id of which client you want him to be managed, new event id, new event type, old event id and old event type");
//                    break;
//                case GetBookingSchedule:
//                    o.setResponse("Please enter the id of which client you want the schedule from");
//                    break;
//                case AddEvent:
//                    o.setResponse("Please enter the event id, event type, and event capacity");
//                    break;
//                case RemoveEvent:
//                    o.setResponse("Please enter the event id and event type");
//                    break;
//                case ListEventAvailability:
//                    o.setResponse("Please enter the event type");
//                    break;
//
//            }
//        }
//        else{
//            o.setApproved(false);
//            switch(req){
//                case BookEvent:
//                    o.setResponse("Please enter the event id and event type");
//                    break;
//                case CancelEvent:
//                    o.setResponse("Please enter the event id and event type");
//                    break;
//                case GetBookingSchedule:
//                    break;
//                case SwapEvent:
//                    o.setResponse("Please enter the new event id, new event type, old event id and old event type");
//                    break;
//            }
//
//        }
//        return o;
//    }


    public MyJsonObject getBookingSchedule(String cid, String mid){
        Date date = new Date();
        MyJsonObject o = new MyJsonObject();
        o.setClientId(cid);
        o.setManagerId(mid);
        o.setRequest(GetBookingSchedule);
        Branch clientBranch = Branch.getBranchFromString(cid.substring(0,3));

        if(!mid.equals("") ){
            if(clientList.get(mid) == null){
                createClient(mid);
            }
        }

        if(clientBranch != this.branch){//client is on another server
            System.out.println("Client is not in this server");
//            o.setTargetServer(clientBranch);
//            MyJsonObject obj =  send(o);
//            obj.setTargetServer(null);
            o.setResponse("Access denied");
            o.setApproved(false);

            logAction(date, o, true, true);//manager has the copy
            return o;
        }

        System.out.println("Client should be in this server");
        Client client = clientList.get(cid);
        if(client == null){
//            o.setResponse("No such client");
//            o.setApproved(false);
            createClient(cid);
            client = clientList.get(cid);
        }




//        else {
            synchronized (client) {
                String temp = "";
                for (String et : client.eventList.keySet()) {
                    for (String eid : client.eventList.get(et).keySet()) {
                        temp += Event.dateToString(client.eventList.get(et).get(eid)) + ", " + eid + ", " + et + "\n";
                    }

                }
                o.setResponse(temp);
                o.setApproved(true);

            }
//        }
        logAction(date, o, true, true);

        return o;



    }

    public MyJsonObject addEvent(String id, String eventId, String eventType, int capacity){
        Date date = new Date();
        MyJsonObject o = new MyJsonObject();
        o.setClientId(id);
        o.setEventId(eventId);
        o.setEventType(eventType);
        o.setCapacity(capacity);
        o.setRequest(AddEvent);

        if(clientList.get(id) == null){
            createClient(id);
        }


        if(!eventId.substring(0,3).equals(this.branch.name())){
            o.setResponse("Access denied");
            o.setApproved(false);
            logAction(date, o, true, true);
            return o;
        }
        System.out.println("Event should be added in this server");
        String rescode = eventRecords.addEvent(eventId, eventType, capacity);
        o.setApproved(false);
        switch (rescode) {
            case "200":
                o.setResponse("Succeed, the event is added");
                o.setApproved(true);
                break;
            case "403":
                o.setResponse("The event has been added already");
                break;
            case "300":
                o.setResponse("The event has been added already, the capcity is updated");
                break;
            default:
                return null;

        }
        logAction(date, o, true, true);
        return o;

    }
    public MyJsonObject removeEvent(String id, String eventId, String eventType){
        Date date = new Date();
        MyJsonObject o = new MyJsonObject();
        o.setClientId(id);
        o.setEventId(eventId);
        o.setEventType(eventType);
        o.setRequest(RemoveEvent);

        if(clientList.get(id) == null){
            createClient(id);
        }


        if(!eventId.substring(0,3).equals(this.branch.name())){
            o.setResponse("Access denied");
            o.setApproved(false);
            logAction(date, o, true, true);
            return o;
        }
        System.out.println("Event should be removed in this server");
        String rescode = eventRecords.removeEvent(eventId, eventType);
        o.setApproved(false);
        switch (rescode) {
            case "200":
                o.setResponse("Succeed, the event is removed");
                o.setApproved(true);
                o.setRequest(ModifyClientList);
                o.setOriginalRequest(RemoveEvent);
                ArrayList<Branch> br = new ArrayList<>(Arrays.asList(Branch.values()));
                br.remove(this.branch);
                o.setTargetServer(br.remove(0));
                send(o);
                o.setTargetServer(br.remove(0));
                send(o);
                o.setOriginalRequest(null);
                o.setRequest(RemoveEvent);
                logAction(date, o, true, false);
                break;
            case "401":
                o.setResponse("Cannot find the event");
                logAction(date, o, true, true);
                break;
            default:
                return null;

        }
        return o;


    }
    public MyJsonObject listEventAvailability(String id, String eventType){
        Date date = new Date();
        MyJsonObject a = new MyJsonObject();
        MyJsonObject b = new MyJsonObject();

        if(clientList.get(id) == null){
            createClient(id);
        }

        ArrayList<Branch> br = new ArrayList<>(Arrays.asList(Branch.values()));
        br.remove(this.branch);
        a.setTargetServer(br.remove(0));
        b.setTargetServer(br.remove(0));
        a.setClientId(id);
        a.setEventType(eventType);
        a.setRequest(GetEventList);
        b.setClientId(id);
        b.setEventType(eventType);
        b.setRequest(GetEventList);
        String res = eventRecords.listAvailability(eventType);
        a = send(a);
        b = send(b);
        a.setTargetServer(null);
        a.setResponse(res + a.getResponse() + b.getResponse());
        logAction(date, a, true, false);


        return a;




    }

    public MyJsonObject cancelEvent(String id, String manId, String eventId, String eventTypeString) {
        Date date = new Date();
        MyJsonObject o = new MyJsonObject();
        o.setManagerId(manId);
        o.setClientId(id);
        o.setEventId(eventId);
        o.setEventType(eventTypeString);
        o.setRequest(CancelEvent);

        if(!manId.equals("") ){
            if(clientList.get(manId) == null){
                createClient(manId);
            }
        }
        Branch clientBranch = Branch.getBranchFromString(id.substring(0,3));
        Branch eventBranch = Branch.getBranchFromString(eventId.substring(0,3));
        if(clientBranch != this.branch){//client is on another server
            System.out.println("Client is not in this server");
//            o.setTargetServer(clientBranch);
//            MyJsonObject obj =  send(o);
//            obj.setTargetServer(null);
            o.setResponse("Access denied");
            o.setApproved(false);
            logAction(date, o, true, true);//manager has the copy
            return o;
        }
        System.out.println("Client should be in this server");
        Client client = clientList.get(id);
        if(client == null){
            createClient(id);
            client = clientList.get(id);
//            o.setResponse("No such client");
//            o.setApproved(false);
        }
//        else {

            String resCode = removeEventFromClient(eventBranch, client, o);
            o.setApproved(false);
            switch (resCode) {
                case "200":
                    o.setResponse("Succeed, client " + client.id + " is removed from the event");
                    o.setApproved(true);
                    break;
                case "401":
                    o.setResponse("Cannot find the event");
                    break;
                case "403":
                    o.setResponse("The client " + id + " is not registered for this event");
                    break;
                case "404":
                    o.setResponse("The client " + id + " have been registered three or more events on other branches");
                    break;
                default:
                    return null;

            }




//        }

        logAction(date, o, true, eventBranch == this.branch);
        return o;



    }
    public MyJsonObject swapEvent(String cid, String mid, String neid, String net, String oeid, String oet){
        Date date = new Date();
        MyJsonObject o = new MyJsonObject();
        o.setManagerId(mid);
        o.setClientId(cid);
        o.setRequest(SwapEvent);
        o.setOldEventId(oeid);
        o.setOldEventType(oet);
        if(!mid.equals("")){
            if(clientList.get(mid) == null){
                createClient(mid);
            }
        }

        Branch clientBranch = Branch.getBranchFromString(cid.substring(0,3));
        Branch newEventBranch = Branch.getBranchFromString(neid.substring(0,3));
        Branch oldEventBranch = Branch.getBranchFromString(oeid.substring(0,3));
        if(clientBranch != this.branch){//client is on another server
            System.out.println("Client is not in this server");
//            o.setTargetServer(clientBranch);
//            MyJsonObject obj =  send(o);
//            obj.setTargetServer(null);
            o.setResponse("Access denied");
            o.setApproved(false);
            o.setEventId(neid);
            o.setEventType(net);
            logAction(date, o, true, true);//manager has the copy
            return o;
        }
        System.out.println("Client should be in this server");
        Client client = clientList.get(cid);
        if(client == null){
//            o.setResponse("No such client");
//            o.setApproved(false);
            createClient(cid);
            client = clientList.get(cid);
        }
//        else {
            synchronized(client) {
                o.setEventId(neid);
                o.setEventType(net);
                String resCode1 = addEventToClient(newEventBranch, client, o);
                if(resCode1.equals("200")) {
                    o.setEventId(oeid);
                    o.setEventType(oet);
                    String resCode2 = removeEventFromClient(oldEventBranch, client, o);
                    o.setApproved(false);
                    switch(resCode2){
                        case "200":
                            o.setResponse("Swapping is Succeed");
                            o.setApproved(true);
                            break;
                        case "401":
                            o.setEventId(neid);
                            o.setEventType(net);
                            removeEventFromClient(newEventBranch, client, o);
                            o.setResponse("Cannot find the old event");
                            break;
                        case "403":
                            o.setEventId(neid);
                            o.setEventType(net);
                            removeEventFromClient(newEventBranch, client, o);
                            o.setResponse("The client " + cid + " is not registered for the old event");
                            break;

                        default:
                            return null;

                    }


                }
                else{
                    o.setApproved(false);
                    switch(resCode1){
                        case "401":
                            o.setResponse("Cannot find the new event");
                            break;
                        case "402":
                            o.setResponse("There is no available seat for the new event");
                            break;
                        case "403":
                            o.setResponse("The client " + cid + " have been registered for the new event already");
                            break;
                        case "404":
                            o.setResponse("The client " + cid + " have been registered three or more events on other branches");
                            break;
                        default:
                            return null;
                    }
                }
            }





//        }
        o.setEventId(neid);
        o.setEventType(net);
        logAction(date, o, true,true);

        return o;



    }

    private MyJsonObject getEventList(MyJsonObject obj){
        obj.setResponse(eventRecords.listAvailability(obj.getEventType()));
        obj.setApproved(true);


        return obj;

    }
    public MyJsonObject bookEvent(String id, String manId, String eventId, String eventTypeString){
        Date date = new Date();
        MyJsonObject o = new MyJsonObject();
        o.setManagerId(manId);
        o.setClientId(id);
        o.setEventId(eventId);
        o.setEventType(eventTypeString);
        o.setRequest(BookEvent);
        if(!manId.equals("")){
            createClient("manId");
        }

        Branch clientBranch = Branch.getBranchFromString(id.substring(0,3));
        Branch eventBranch = Branch.getBranchFromString(eventId.substring(0,3));
        if(clientBranch != this.branch){//client is on another server
            System.out.println("Client is not in this server");
//            o.setTargetServer(clientBranch);
//            MyJsonObject obj =  send(o);
//            obj.setTargetServer(null);
            o.setResponse("Access denied");
            o.setApproved(false);
            logAction(date, o, true, true);//manager has the copy
            return o;
        }
        System.out.println("Client should be in this server");
        Client client = clientList.get(id);
        if(client == null){
//            o.setResponse("No such client");
//            o.setApproved(false);
            createClient(id);
            client = clientList.get(id);
        }
//        else {

            String resCode = addEventToClient(eventBranch, client, o);
            o.setApproved(false);
            switch (resCode) {
                case "200":
                    o.setResponse("Succeed, client " + client.id + " is registered for the event");
                    o.setApproved(true);
                    break;
                case "401":
                    o.setResponse("Cannot find the event");
                    break;
                case "402":
                    o.setResponse("There is no available seat for this event");
                    break;
                case "403":
                    o.setResponse("The client " + id + " have been registered for this event already");
                    break;
                case "404":
                    o.setResponse("The client " + id + " have been registered three or more events on other branches");
                    break;
                default:
                    return null;

            }




//        }
        logAction(date, o, true,eventBranch == this.branch);

        return o;


        }
    private String addEventToClient(Branch toBranch, Client client,MyJsonObject o){
        synchronized(client) {
            if (!client.eventList.get(o.getEventType()).containsKey(o.getEventId())) {
                Date da = Event.parseEventDate(o.getEventId());
                if (client.checkDate(da) || o.getEventId().substring(0,3).equals(client.branch.name())){
                    //event is on another branch
                    if(toBranch != this.branch) {
                        o.setOriginalRequest(BookEvent);
                        o.setRequest(ModifyEventList);
                        o.setTargetServer(toBranch);
                        MyJsonObject res = send(o);
                        if (res.isApproved()) {
                            client.eventList.get(o.getEventType()).put(o.getEventId(), da);
                        }
                        return res.getResponse();
                    }else{
                        String code = eventRecords.addClient(o.getEventId(), o.getEventType());
                        if(code.equals("200")){
                            client.eventList.get(o.getEventType()).put(o.getEventId(), da);
                        }
                        return code;
                    }

                }
                else{
                    return "404";
                }

            } else {
                return "403";
            }
        }
    }
    private String removeEventFromClient(Branch toBranch, Client client,MyJsonObject o){
        synchronized(client) {
            if (client.eventList.get(o.getEventType()).containsKey(o.getEventId())) {
                if(toBranch != this.branch) {//event is on another branch
                    o.setOriginalRequest(CancelEvent);
                    o.setRequest(ModifyEventList);
                    o.setTargetServer(toBranch);
                    MyJsonObject res = send(o);
                    //TODO
                    if (res.isApproved()) {
                        client.eventList.get(o.getEventType()).remove(o.getEventId());
                    }
                    return res.getResponse();
                }
                else{
                    String code = eventRecords.removeClient(o.getEventId(), o.getEventType());
                    if(code.equals("200")){
                        client.eventList.get(o.getEventType()).remove(o.getEventId());
                    }
                    return code;
                }

            }


            else {
                return "403";
            }
        }
    }
    private MyJsonObject send(MyJsonObject o) {
        Date date = new Date();
        byte[] bytes;
        HashMap<String, String> param = o.toHashMap();
        bytes = MyJsonObject.hashMapToString(param).getBytes();
        int toPort;
        if(o.getTargetServer() != null){//send request
            toPort = o.getTargetServer().getUdpReplyPort();
            System.out.println("Sending request " + o.getRequest().name() + " to branch " + o.getTargetServer().name());


        }
        else{//reply
            toPort = o.getSourceServer().getUdpSendPort();
            System.out.println("Sending response to request " + o.getRequest().name() + " to branch " + o.getSourceServer().name());
        }
        try {
            InetAddress host = InetAddress.getByName("localhost");
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length, host, toPort);
            if(o.getTargetServer() != null) {
                socketSend.send(dp);
            }
            else {
                socketReply.send(dp);
            }
            logAction(date, o, false, true);//server log on sending
            if(o.getTargetServer() != null){//wait for the response
                byte[] buffer = new byte[1024];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                socketSend.receive(reply);
                System.out.println("Receive response from branch " + Branch.getBranchFromPort(reply.getPort()).name());
                MyJsonObject replyObj = parsePacket(reply);
                replyObj.setSourceServer(null);
                replyObj.setTargetServer(o.getTargetServer());
                logAction(new Date(), replyObj, false, true);//server log on receiving
                return replyObj;
            }



        }


         catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    protected boolean isManager(String id){
        return id.charAt(3) == 'M';
    }





    private class Client {
        String id;
        Log log;
        Branch branch;


        HashMap<String, HashMap<String, Date>> eventList;//event type, event id, eventDate

        Client(String id) {
            eventList = new HashMap<>();//
            for (String s : Event.EVENTTYPES) {
                eventList.put(s, new HashMap<>());
            }
            this.id = id;
            log = new Log(System.getProperty("user.dir") + "/" + id.substring(0, 3) + "/" + id + ".txt");
            branch = Branch.getBranchFromString(id.substring(0, 3));

        }


        synchronized boolean checkDate(Date date) {
            Date upper, lower;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, 1);
            upper = cal.getTime();
            cal.add(Calendar.MONTH, -2);
            lower = cal.getTime();

            int count = 0;

            for (String et : eventList.keySet()) {
                for (String eventId : eventList.get(et).keySet()) {
                    if (!eventId.substring(0, 3).equals(branch.name())) {
                        if (eventList.get(et).get(eventId).compareTo(lower) > 0 && eventList.get(et).get(eventId).compareTo(upper) < 0) {
                            count++;
                        }
                    }
                }

            }
            if (count >= 3) {
                return false;
            } else {
                return true;
            }


        }
        void removeEvent(String eid, String et){
            eventList.get(et).remove(eid);
        }

    }

}














