import java.util.concurrent.atomic.AtomicInteger;

public class VirtualThreadExample {

    private static void task() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int numTasks = 1000;
        int numThreads = 4;

        AtomicInteger virtualThreadCounter = new AtomicInteger();
        AtomicInteger platformThreadCounter = new AtomicInteger();

        Runnable platformThreadTask = () -> {
            task();
            platformThreadCounter.incrementAndGet();
        };

        Runnable virtualThreadTask = () -> {
            task();
            virtualThreadCounter.incrementAndGet();
        };

        long platformStartTime = System.currentTimeMillis();
        Thread[] platformThreads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            platformThreads[i] = new Thread(platformThreadTask);
            platformThreads[i].start();
        }

        while (System.currentTimeMillis() - platformStartTime < 5000) {}

        for (Thread thread : platformThreads) {
            thread.join();
        }

        long virtualStartTime = System.currentTimeMillis();
        Thread[] virtualThreads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            virtualThreads[i] = Thread.ofVirtual().start(virtualThreadTask);
        }

        while (System.currentTimeMillis() - virtualStartTime < 5000) {}

        for (Thread thread : virtualThreads) {
            thread.join();
        }

        long elapsedTime = System.currentTimeMillis() - Math.max(platformStartTime, virtualStartTime);
        System.out.println("Elapsed time: " + elapsedTime + "ms");
        System.out.println("Virtual Threads completed: " + virtualThreadCounter.get());
        System.out.println("Platform Threads completed: " + platformThreadCounter.get());
    }
}
