package FE;

import java.net.DatagramSocket;

public class Timer implements Runnable{

    volatile Boolean timeout;
    DatagramSocket socket;

    public Timer(DatagramSocket socket ,Boolean timeout){
        this.socket = socket;
        this.timeout = timeout;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            timeout = true;
            socket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
