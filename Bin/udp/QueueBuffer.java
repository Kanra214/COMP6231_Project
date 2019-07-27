package udp;

import java.util.LinkedList;

public class QueueBuffer {
    private LinkedList queue;
    public QueueBuffer(){
        queue = new LinkedList();
    }
    public void addElement(Object element){

         adjustSize();
    }
    private void adjustSize(){
        int currentSize = getSize();
        while(currentSize >= 100){
            removeFirst();
        }
    }
    private synchronized void add(Object element){
        queue.addLast(element);
    }
    public synchronized int getSize()
    {
        return queue.size();
    }
    public synchronized Object removeFirst(){
        return queue.removeFirst();
    }
    public synchronized Object peekFirst(){
        return queue.peekFirst();
    }
    public synchronized void remove(String element){
        queue.remove(element);
    }
    public synchronized boolean contains(String element) { return queue.contains(element); }
}