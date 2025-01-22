public class CpuStressTest {
    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Starting stress test on " + cores + " cores.");

        for (int i = 0; i < cores; i++) {
            new Thread(() -> {
                while (true) {

                }
            }).start();
        }

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
