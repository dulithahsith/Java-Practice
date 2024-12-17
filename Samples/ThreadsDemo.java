class Counter{
    private int initial =0;
    public void increment(){
        initial++;
    }
    public void decrement(){
        initial--;
    }
    public int get_init(){
        return this.initial;
    }
}

public class ThreadsDemo{
    public static void main(String[] args) throws InterruptedException {
        Counter c = new Counter();
        Runnable a = () -> {
            for (int i =0 ; i<10;i++) {
                c.increment();
            }
        };
        Runnable b = () ->{
            for (int i =0 ; i<10;i++) {
                c.decrement();
            }
        };
        Thread t1 = new Thread(a);
        Thread t2 = new Thread(b);
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(c.get_init());
    }
}