import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Job implements Runnable{
    String name;
    public Job(String name){
        this.name = name;
    }
    @Override
    public void run() {
        System.out.println(name+" is getting executed at "+ LocalDateTime.now());
    }
}

public class ScheduledThreadPool_Ex {

    public static void main(String[] args){
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);

        Runnable r1 = new Job("Task1");
        Runnable r2 = new Job("Task2");
        Runnable r3 = new Job("Task3");

        pool.schedule(r1, 2 , TimeUnit.SECONDS);
        pool.schedule(r2, 6 , TimeUnit.SECONDS);
        pool.schedule(r3, 10 , TimeUnit.SECONDS);

        pool.scheduleAtFixedRate(r1,10,3,TimeUnit.SECONDS);
    }

}
