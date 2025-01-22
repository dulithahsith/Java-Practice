import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class Task implements Runnable{
    private String name;
    public Task(String name){
        this.name = name;
    }

    @Override
    public void run() {
        for(int i=0; i<=5; i++){
            if(i==0){System.out.println(name+" Starting time: "+ LocalDateTime.now());}
            else{
            System.out.println(name+" Executing time: "+ LocalDateTime.now());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
public class FixedThreadPool_Ex {
    public static void main(String[] args){
        ExecutorService pool = Executors.newFixedThreadPool(3);

        Runnable r1 = new Task("task 1");
        Runnable r2 = new Task("task 2");
        Runnable r3 = new Task("task 3");
        Runnable r4 = new Task("task 4");
        Runnable r5 = new Task("task 5");

        pool.execute(r1);
        pool.execute(r2);
        pool.execute(r3);
        pool.execute(r4);
        pool.execute(r5);
    }


}
