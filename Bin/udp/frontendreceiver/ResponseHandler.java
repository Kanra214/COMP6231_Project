package udp.frontendreceiver;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import udp.RequestBuffer;
import udp.ServerActionConstants;
import udp.UdpResponseType;

import java.net.DatagramPacket;
import java.util.Arrays;

public class ResponseHandler implements Runnable {
    DatagramPacket datagramPacket;
    RequestBuffer requestBuffer;
    public ResponseHandler(DatagramPacket datagramPacket, RequestBuffer requestBuffer){
        this.datagramPacket = datagramPacket;
        this.requestBuffer = requestBuffer;
    }

    @Override
    public void run() {
        JSONObject responseJson = getParsedJSON(datagramPacket.getData());
        UdpResponseType responseType = UdpResponseType.valueOf((String)responseJson.get(ServerActionConstants.REPLICA_RESPONSE_TYPE));
        if(responseType == null){
            return;
        }
        switch(responseType){
            case SERVANT_MULTICAST_ACKNOWLEDGEMENT:
                processAcknowledgement(responseJson);
                break;
            case SERVANT_RESPONSE:
                storeResponse(responseJson);
                break;
            default:
                System.out.println("Improper response type");
        }
    }

    //Store response to history buffer
    private void storeResponse(JSONObject response){
        String requestId = (String)response.get(ServerActionConstants.REQUEST_ID);
        response.remove(ServerActionConstants.REQUEST_ID);
        requestBuffer.storeResponse(requestId, response);
        System.out.println("String Response: " + response.toJSONString().toString());
        notifyFrontEndIfReceivedAllResponses(requestId);
    }

    //Notify FE that received all responses
    private void notifyFrontEndIfReceivedAllResponses(String requestId){
        if(requestBuffer.getResponseCount(requestId) == 3){
            requestBuffer.notifyFrontEndOnReceivingAllResponses(requestId);
        }
    }

    //Inform Sequencer that received acknowledgement
    private void processAcknowledgement(JSONObject ack){
        String replicaId = (String) ack.get(ServerActionConstants.REPLICA_ID);
        String requestId = (String) ack.get(ServerActionConstants.REQUEST_ID);
        requestBuffer.removeAcknowledgedPacket(replicaId, requestId);
    }

    private JSONObject getParsedJSON(byte[] data){
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(new String(data).trim());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return response;
    }
}