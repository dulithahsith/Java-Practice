package org.example.ex2;

import java.util.Queue;
import java.util.LinkedList;

class Breads{
    int capacity;
    Queue<String> breads;

    public Breads(int capacity){
        this.capacity = capacity;
        this.breads = new LinkedList<>();
    }
    public synchronized void produce(String bread){
        while(breads.size()==capacity){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        breads.add(bread);
        System.out.println("Produced: "+bread);
        notify();
    }
    public synchronized void consume(){
        while(breads.isEmpty()){
            try {
                System.out.println("Customer is waiting...");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String bread = breads.poll();
        System.out.println("Consumed: "+bread);
        notify();
    }
}

public class ex2{
    public static void main(String[] args) throws InterruptedException {
        Breads breads = new Breads(5);

        Thread producer = new Thread(()->{
            for (int i=1;i<=10;i++){
                StringBuilder sb = new StringBuilder("Bread #");
                String bread = String.valueOf(sb.append(i));
                breads.produce(bread);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread customer = new Thread(()->{
            for (int i=1;i<=10;i++){
                breads.consume();
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        producer.start();
        customer.start();

        producer.join();
        customer.join();
    }
}

