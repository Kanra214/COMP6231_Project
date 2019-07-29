package RMs.src;

import RMs.src.RMID;
import RMs.src.ReplicaManager;

import java.net.SocketException;

public class RMDriver {//TODO:split this to 3 hosts, figure out ips and give them to sequencer and FE
    public static void main(String[] args) throws SocketException {
        ReplicaManager xiyun1 = new ReplicaManager(RMID.Xiyun1);
        ReplicaManager xiyun2 = new ReplicaManager(RMID.Xiyun2);
        ReplicaManager bin = new ReplicaManager(RMID.BinServer);
        new Thread(new RMStarter(xiyun1)).start();
        new Thread(new RMStarter(xiyun2)).start();
        new Thread(new RMStarter(bin)).start();

    }
    static class RMStarter implements Runnable{
        private ReplicaManager r;
        RMStarter(ReplicaManager r){
            this.r = r;
        }

        @Override
        public void run() {
            r.start();
        }
    }
}
