package Common;
import java.util.HashMap;


public class JsonObject {//only use public methods please
    private String clientId;
    private String managerId;
    private String response;
    private Requests request;
    private String eventType;
    private String eventId;
    private String oldEventId;
    private String oldEventType;
    private int capacity;
    private int seqNum;



    private String sourceIp;
    private int sourcePort;
    private String portString;
    private String capacityString;

    private String seqNumString;
    private String approved;
    public String getApproved() {
        return approved;
    }

    //getters
    public String getClientId() {
        return clientId;
    }

    public String getManagerId() {
        return managerId;
    }

    public boolean isApproved(){
        if (approved.equals("True")) {
            return true;
        } else {
            return false;
        }

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

    public int getSeqNum() {
        return seqNum;
    }

    public String getSourceIp() {
        return sourceIp;
    }
    public int getSourcePort(){
        return sourcePort;
    }


    public void setCapacity(int capacity) {
        this.capacityString = capacity + "";
        this.capacity = capacity;
    }



    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setManagerId(String managerId) {
        if(managerId != null) {
            if (!managerId.equals("")) {
                this.managerId = managerId;
            }
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

    public void setSourceIp(String ip) {
        this.sourceIp = ip;
    }
    public void setSourcePort(int port){
        this.sourcePort = port;
    }
    public void setSeqNum(int seqNum) {
        this.seqNumString = seqNum + "";
        this.seqNum = seqNum;
    }







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
        if(capacityString != null){
            hash.put("Capacity", capacityString);
        }
        if(oldEventId != null){
            hash.put("OldEventId", oldEventId);
        }
        if(oldEventType != null){
            hash.put("OldEventType", oldEventType);
        }
        if(sourceIp != null){
            hash.put("SourceIp", sourceIp);
        }
        if(sourceIp != null){
            hash.put("SourcePort", sourcePort + "");
        }
        if(seqNumString != null){
            hash.put("SeqNum", seqNumString);
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
                if(k.equals("Capacity")){
                    this.setCapacity(Integer.parseInt(hash.get("Capacity")));
                }
                if(k.equals("OldEventId")){
                    this.setOldEventId(hash.get("OldEventId"));
                }
                if(k.equals("OldEventType")){
                    this.setOldEventType(hash.get("OldEventType"));
                }
                if(k.equals("SourceIp")){
                    this.setSourceIp(hash.get("SourceIp"));
                }
                if(k.equals("SourcePort")){
                    this.setSourcePort(Integer.parseInt(hash.get("SourcePort")));
                }
                if(k.equals("SeqNum")){
                    this.setSeqNum(Integer.parseInt(hash.get("SeqNum")));
                }
            }
        }
    }



    public static String hashMapToString(HashMap<String, String> has){
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
    public String objectToString(){
       return hashMapToString(this.toHashMap());
    }

    public static JsonObject stringToObject(String s){
        JsonObject o = new JsonObject();
        o.setFromHashMap(stringToHashMap(s));
        return o;
    }




}
