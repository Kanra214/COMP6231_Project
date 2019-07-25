package Servers;

import Common.*;

import java.util.HashMap;

public class MyJsonObject extends JsonObject{
    private Requests originalRequest;
    private Branch targetServer;
    private Branch sourceServer;


    public void setOriginalRequest(Requests originalRequest) {
        this.originalRequest = originalRequest;
    }

    public void setTargetServer(Branch targetServer) {
        this.targetServer = targetServer;
    }

    public void setSourceServer(Branch sourceServer) {
        this.sourceServer = sourceServer;
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






    @Override
    public HashMap<String, String> toHashMap(){

        HashMap<String, String> hash = super.toHashMap();

        if(originalRequest != null){
            hash.put("OriginalRequest", originalRequest.name());
        }
        if(targetServer != null){
            hash.put("TargetServer", targetServer.name());
        }
        if(sourceServer != null){
            hash.put("SourceServer", sourceServer.name());
        }

        return hash;
    }

    public void setFromHashMap(HashMap<String, String> hash){
        if(hash != null){
            super.setFromHashMap(hash);
            for(String k : hash.keySet()){
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

            }
        }
    }




    public static String objectToString(MyJsonObject obj){
       return hashMapToString(obj.toHashMap());
    }
    public static MyJsonObject stringToObject(String s){
        MyJsonObject o = new MyJsonObject();
        o.setFromHashMap(stringToHashMap(s));
        return o;
    }




}
