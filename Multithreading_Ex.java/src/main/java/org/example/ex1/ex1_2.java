package org.example.ex1;

import java.util.concurrent.atomic.AtomicInteger;

class Workker{
    AtomicInteger num = new AtomicInteger(1);

    public void odd_inc() throws InterruptedException{
        while(num.get()<10){
            if(num.get()%2==0){
                System.out.println("\nThread 0: "+num.get());
                num.addAndGet(1);
            }
        }
    }
    public void even_inc() throws InterruptedException{
        while(num.get()<10){
            if(num.get()%2==1){
                System.out.println("\nThread 1: "+num.get());
                num.addAndGet(1);
            }
        }
    }
}
public class ex1_2{
    public static void main(String[] args){
        Workker w = new Workker();
        Thread t1 = new Thread(() -> {
            try {
                w.odd_inc();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                w.even_inc();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}