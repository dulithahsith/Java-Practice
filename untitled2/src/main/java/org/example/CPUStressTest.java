package org.example;

public class CPUStressTest {
    public static void main(String args[]){
        int cores = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[cores];
        for (int i=0; i<cores;i++){
            threads[i] = new Thread(()->{
                while(true){

                }
            });
            threads[i].start();
        }

        for (int i=0; i<cores;i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
