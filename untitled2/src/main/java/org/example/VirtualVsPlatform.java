package org.example;

public class VirtualVsPlatform {
    private int platformCount=0;
    private int virtualCount = 0;
    public void platformThreadCounter(){
        platformCount++;
        platformThreadCounter();
    }
    public void virtualThreadCounter(){
        virtualCount++;
        virtualThreadCounter();
    }
    public void test(){
        System.out.println("Start-> Platform Thread");
        Thread t1 = new Thread(()->{
            try{
                platformThreadCounter();
            }
            catch (StackOverflowError e){
                System.out.println("Platform-> "+platformCount);
            }
        });

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Start-> Virtual Thread");
        Thread t2 = Thread.ofVirtual().start(()->{
            try{
                virtualThreadCounter();
            }
            catch (StackOverflowError e){
                System.out.println("Virtual-> "+virtualCount);
            }
        });

        try {
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args){
        VirtualVsPlatform vp = new VirtualVsPlatform();
        vp.test();
    }
}
