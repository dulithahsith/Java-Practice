package org.example.ex1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Worker {
    private static final ReentrantLock re = new ReentrantLock();
    private static final Condition isOdd = re.newCondition();


    int number = 0;


    public void odd_even_inc() throws InterruptedException {
        while (number < 10) {
            number += 1;
            re.lock();
            try{

            }
            catch (InterruptedException e){

            }
            finally {

            }
            if (Thread.currentThread().getName().equals("Thread-0")) {
                if (number % 2 == 0) {
                    System.out.println("\nThread-0 :" + number);
                    notify();
                } else {
                    wait();
                }
            } else {
                if (number % 2 == 1) {
                    System.out.println("\nThread-1 :" + number);
                    notify();
                } else {
                    wait();
                }
            }
        }
    }
}

public class ex1{
    public static void main(String[] args) throws InterruptedException {
        Worker worker = new Worker();
        Thread t1 = new Thread(()-> {
            try {
                worker.odd_even_inc();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"Thread-0");
        Thread t2 = new Thread(()-> {
            try {
                worker.odd_even_inc();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"Thread-1");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}