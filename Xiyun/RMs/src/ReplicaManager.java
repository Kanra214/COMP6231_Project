import Common.JsonObject;
import Servers.Server;

import java.util.PriorityQueue;

public class ReplicaManager {
    private RMID rmid = RMID.Xiyun1;
    PriorityQueue<JsonObject> MTLdeliverQueue, TORdeliverQueue, OTWdeliverQueue,
            MTLholdbackQueue, TORholdbackQueue, OTWholdbackQueue;
    Server MTLServer, TORServer, OTWServer;

}
