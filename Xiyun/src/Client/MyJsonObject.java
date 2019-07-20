package Client;

import java.io.Serializable;
import java.util.HashMap;

public class MyJsonObject implements Serializable {

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setManagerId(String managerId) {
        if(!managerId.equals("")){
            this.managerId = managerId;
        }

    }

    public void setApproved(boolean b) {
        if(b) {
            this.approved = "True";
        }
        else{
            this.approved = "False";
        }
    }

    public void setResponse(String response) {
        if(response != "") {
            this.response = response;
        }
    }

    public void setRequest(Requests request) {
        this.request = request;
    }

    public void setOriginalRequest(Requests originalRequest) {
        this.originalRequest = originalRequest;
    }

    public void setTargetServer(Branch targetServer) {
        this.targetServer = targetServer;
    }

    public void setSourceServer(Branch sourceServer) {
        this.sourceServer = sourceServer;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setOldEventId(String oldEventId) {
        this.oldEventId = oldEventId;
    }

    public void setOldEventType(String oldEventType) {
        this.oldEventType = oldEventType;
    }


    public String getClientId() {
        return clientId;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getApproved() {
        return approved;
    }

    public String getResponse() {
        if(response == null) {
            return "";
        }
        return response;
    }

    public Requests getRequest() {
        return request;
    }

    public Requests getOriginalRequest() {
        return originalRequest;
    }

    public Branch getTargetServer() {
        return targetServer;
    }

    public Branch getSourceServer() {
        return sourceServer;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventId() {
        return eventId;
    }
    public int getCapacity() {
        return capacity;
    }

    public String getOldEventId() {
        return oldEventId;
    }

    public String getOldEventType() {
        return oldEventType;
    }
    public void setCapacity(int capacity) {
        this.capacityString = capacity + "";
        this.capacity = capacity;
    }


    private String clientId;
    private String managerId;
    private String approved;
    private String response;
    private Requests request;
    private Requests originalRequest;
    private Branch targetServer;
    private Branch sourceServer;
    private String eventType;
    private String eventId;
    private String capacityString;


    private String oldEventId;
    private String oldEventType;

    private int capacity;




    public HashMap<String, String> toHashMap(){
        HashMap<String, String> hash = new HashMap<>();
        if(request != null){
            hash.put("Request", request.name());
        }
        if(clientId != null){
            hash.put("ClientId", clientId);
        }
        if(managerId != null){
            hash.put("ManagerId", managerId);
        }
        if(eventId != null){
            hash.put("EventId", eventId);
        }
        if(eventType != null){
            hash.put("EventType", eventType);
        }
        if(approved != null){
            hash.put("Approved", approved);
        }
        if(response != null){
            hash.put("Response", response);
        }
        if(originalRequest != null){
            hash.put("OriginalRequest", originalRequest.name());
        }
        if(targetServer != null){
            hash.put("TargetServer", targetServer.name());
        }
        if(sourceServer != null){
            hash.put("SourceServer", sourceServer.name());
        }
        if(capacityString != null){
            hash.put("Capacity", capacityString);
        }
        if(oldEventId != null){
            hash.put("OldEventId", oldEventId);
        }
        if(oldEventType != null){
            hash.put("OldEventType", oldEventType);
        }
        return hash;
    }

    public void setFromHashMap(HashMap<String, String> hash){
        if(hash != null){
            for(String k : hash.keySet()){
                if(k.equals("Request")){
                    this.setRequest(Requests.getRequest(hash.get("Request")));
                }
                if(k.equals("ClientId")){
                    this.setClientId(hash.get("ClientId"));
                }
                if(k.equals("ManagerId")){
                    this.setManagerId(hash.get("ManagerId"));
                }
                if(k.equals("EventId")){
                    this.setEventId(hash.get("EventId"));
                }
                if(k.equals("EventType")){
                    this.setEventType(hash.get("EventType"));
                }
                if(k.equals("Approved")){
                    this.setApproved(hash.get("Approved").equals("True"));
                }
                if(k.equals("Response")){
                    this.setResponse(hash.get("Response"));

                }
                if(k.equals("OriginalRequest")){
                    this.setOriginalRequest(Requests.getRequest(hash.get("OriginalRequest")));
                }
                if(k.equals("TargetServer")){
                    this.setTargetServer(Branch.getBranchFromString(hash.get("TargetServer")));
                }
                if(k.equals("SourceServer")){
                    this.setTargetServer(Branch.getBranchFromString(hash.get("SourceServer")));
                }
                if(k.equals("Capacity")){
                    this.setCapacity(Integer.parseInt(hash.get("Capacity")));
                }
                if(k.equals("OldEventId")){
                    this.setOldEventId(hash.get("OldEventId"));
                }
                if(k.equals("OldEventType")){
                    this.setEventType(hash.get("OldEventType"));
                }
            }
        }
    }

    public boolean isApproved(){
        if (approved.equals("True")) {
            return true;
        } else {
            return false;
        }

    }

    public static String hashToString(HashMap<String, String> has){
        String input = "";
        if(has != null){
            for(String k : has.keySet()){
                input += k + ":" + has.get(k) + ";";
            }
        }
        return input;
    }

    public static HashMap<String, String> stringToHashMap(String ss){
        String[] temp1 = ss.split(";");
        HashMap<String, String> param = new HashMap<>();
        for(String s : temp1){
            String[] temp2 = s.split(":");
            param.put(temp2[0], temp2[1]);
        }
        return param;
    }
    public static String objectToString(MyJsonObject obj){
       return hashToString(obj.toHashMap());
    }
    public static MyJsonObject stringToObject(String s){
        MyJsonObject o = new MyJsonObject();
        o.setFromHashMap(stringToHashMap(s));
        return o;
    }




}
