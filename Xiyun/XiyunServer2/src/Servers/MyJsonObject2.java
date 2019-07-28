package Servers;

import Common.JsonObject;
import Common.Requests;

import java.util.HashMap;

public class MyJsonObject2 extends JsonObject{
    private Requests originalRequest;
    private Branch2 targetServer;
    private Branch2 sourceServer;


    public void setOriginalRequest(Requests originalRequest) {
        this.originalRequest = originalRequest;
    }

    public void setTargetServer(Branch2 targetServer) {
        this.targetServer = targetServer;
    }

    public void setSourceServer(Branch2 sourceServer) {
        this.sourceServer = sourceServer;
    }


    public Requests getOriginalRequest() {
        return originalRequest;
    }

    public Branch2 getTargetServer() {
        return targetServer;
    }

    public Branch2 getSourceServer() {
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
                    this.setTargetServer(Branch2.getBranchFromString(hash.get("TargetServer")));
                }
                if(k.equals("SourceServer")){
                    this.setTargetServer(Branch2.getBranchFromString(hash.get("SourceServer")));
                }

            }
        }
    }




    public static String objectToString(MyJsonObject2 obj){
       return hashMapToString(obj.toHashMap());
    }
    public static MyJsonObject2 stringToObject(String s){
        MyJsonObject2 o = new MyJsonObject2();
        o.setFromHashMap(stringToHashMap(s));
        return o;
    }




}
