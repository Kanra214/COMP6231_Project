import java.net.SocketException;

public class RMDriver {
    public static void main(String[] args) throws SocketException {
        ReplicaManager xiyun1 = new ReplicaManager(RMID.Xiyun1);
        ReplicaManager xiyun2 = new ReplicaManager(RMID.Xiyun2);
        ReplicaManager bin = new ReplicaManager(RMID.BinServer);
        xiyun1.start();
        xiyun2.start();
        bin.start();

    }
}
