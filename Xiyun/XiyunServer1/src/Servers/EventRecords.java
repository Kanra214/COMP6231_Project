package Servers;

import java.util.HashMap;

public class EventRecords {
    private HashMap<String, HashMap<String, Event>> hashMap;
    protected EventRecords(){
        hashMap = new HashMap<>();

        for(String s : Event.EVENTTYPES) {
            hashMap.put(s, new HashMap<>());
        }
    }
    protected synchronized String addEvent(String eid, String et, int capacity){


        if(!hashMap.get(et).containsKey(eid)){//if the event exists, update and return false
            hashMap.get(et).put(eid, new Event(capacity, eid, et, null));
            return "200";

        }
        else{
            if(hashMap.get(et).get(eid).getCapacity() != capacity){
                if(hashMap.get(et).get(eid).getScheduled() > capacity) {
                    return "400";
                }
                else{
                    hashMap.get(et).get(eid).setCapacity(capacity);
                    return "300";
                }
            }
            else{
                return "403";
            }
        }





    }

    protected synchronized String removeEvent(String eid, String et){
        if(!hashMap.get(et).containsKey(eid)){
            return "401";
        }
        else{
            hashMap.get(et).remove(eid);
            return "200";
        }
    }

    protected synchronized String addClient(String eventId, String type){
        //1，2 在event server
        //3，4在client server

        HashMap<String, Event> tempHash = hashMap.get(type);
        Event event = tempHash.get(eventId);
        if (event != null) {
            if (event.increment()) {
                return "200";//successed
            }
            else {
                return "402";//exceed the limit
            }
        }
        else {
            return "401";//cannot find the event
        }
    }



    protected synchronized String removeClient(String eventId, String type){
        //1，2 在event server
        //3，4在client server

        HashMap<String, Event> tempHash = hashMap.get(type);
        Event event = tempHash.get(eventId);
        if(event != null){
            if(event.decrement()){
                return "200";
            }
            else{
                System.out.println("negative number");
                return "999";
            }
        }
        else{
            return "401";
        }
    }

    protected synchronized String listAvailability(String et){
        String s = "";
        for(Event ev : hashMap.get(et).values()){
            s += ev.getId() + ", " + (ev.getCapacity() - ev.getScheduled()) + "\n";
        }

        return s;
    }





}













