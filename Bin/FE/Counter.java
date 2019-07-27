package FE;

import java.io.*;

public class Counter implements Serializable{
    private int counter;
    public Counter(int cntr){
        counter = cntr;
    }
    public Counter(){
        counter = 0;
    }
    public synchronized Integer readCounter(){
        return counter;
    }
    public synchronized Integer nextVal()
    {
        return ++counter;
    }
}