import java.util.ArrayList;
import java.util.List;

public class MemoryLeakExample {
    // Static collection to simulate memory leak
    private static List<Object> leakList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Memory Leak Demo...");
        for (int i = 0; i < 1000000; i++) {
            leakList.add(new byte[1024 * 1024]);

            // Simulate some delay
            Thread.sleep(10);

            System.out.println("Added object " + (i + 1) + " to the list");

            if (i % 500 == 0) {
                leakList.clear();
            }
        }
    }
}
