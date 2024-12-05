package org.example.ex3;

class DeadlockDemo{
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void method1(){
        synchronized (lock1){
            System.out.println("method1 has lock1, waiting for lock2");
            synchronized ((lock2)){
                System.out.println("performing method1");
            }
        }
    }
    public void method2(){
        synchronized (lock2){
            System.out.println("method2 has lock1, waiting for lock1");
            synchronized ((lock1)){
                System.out.println("performing method2");
            }
        }
    }

}
public class ex3{
    public static void main(String[] args){
        DeadlockDemo dd = new DeadlockDemo();
        Thread t1 = new Thread(()-> dd.method1());
        Thread t2 = new Thread(()-> dd.method2());

        t1.start();
        t2.start();
    }
}