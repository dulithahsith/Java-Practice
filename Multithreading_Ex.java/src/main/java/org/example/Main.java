package org.example;

class Counter{
    private int count = 0;

    public synchronized void increment(){
        count++;
    }
}

class Worker implements Runnable{
    Counter counter;
    public Worker(Counter counter){
        this.counter = counter;
    }
    @Override
    public void run(){
        counter.increment();
    }
}

public class Main{
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Thread t1 = new Thread(new Worker(counter));
        Thread t2 = new Thread(new Worker(counter));

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}