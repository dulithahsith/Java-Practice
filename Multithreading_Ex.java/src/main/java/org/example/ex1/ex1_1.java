package org.example.ex1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Worker {
    private static final ReentrantLock re = new ReentrantLock();
    private static final Condition isOdd = re.newCondition();
    private static final Condition isEven = re.newCondition();

    int number = 1;

    public void odd_even_inc() throws InterruptedException {
        while (number < 10) {
            re.lock();
            try{
                if (Thread.currentThread().getName().equals("Thread-0")) {//even thread
                    if (number % 2 == 0) {
                        System.out.println("\nThread-0 :" + number);
                        number += 1;
                        isOdd.signal();
                    } else {
                        isEven.await();
                    }
                } else {
                    if (number % 2 == 1) {     // odd thread
                        System.out.println("\nThread-1 :" + number);
                        number += 1;
                        isEven.signal();
                    } else {
                        isOdd.await();
                    }
                }
            }
            finally {
                re.unlock();
            }

        }
    }
}

public class ex1_1{
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