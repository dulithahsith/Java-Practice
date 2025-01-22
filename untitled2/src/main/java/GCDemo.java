public class GCDemo {

    public static void main(String[] args) {
        int iterations = 1000000;

        System.out.println("Running with Serial GC...");
        long startTime = System.nanoTime();
        runTestWithGC("SerialGC");
        long endTime = System.nanoTime();
        System.out.println("Serial GC took: " + (endTime - startTime) / 1000000 + " ms");

        System.out.println("\nRunning with Parallel GC...");
        startTime = System.nanoTime();
        runTestWithGC("ParallelGC");
        endTime = System.nanoTime();
        System.out.println("Parallel GC took: " + (endTime - startTime) / 1000000 + " ms");

        System.out.println("\nRunning with G1 GC...");
        startTime = System.nanoTime();
        runTestWithGC("G1GC");
        endTime = System.nanoTime();
        System.out.println("G1 GC took: " + (endTime - startTime) / 1000000 + " ms");

        System.out.println("\nRunning with CMS GC...");
        startTime = System.nanoTime();
        runTestWithGC("CMSGC");
        endTime = System.nanoTime();
        System.out.println("CMS GC took: " + (endTime - startTime) / 1000000 + " ms");
    }

    public static void runTestWithGC(String gcType) {
        String gcOptions = "";

        switch (gcType) {
            case "SerialGC":
                gcOptions = "-XX:+UseSerialGC";
                break;
            case "ParallelGC":
                gcOptions = "-XX:+UseParallelGC";
                break;
            case "G1GC":
                gcOptions = "-XX:+UseG1GC";
                break;
            case "CMSGC":
                gcOptions = "-XX:+UseConcMarkSweepGC";
                break;
            default:
                throw new IllegalArgumentException("Unsupported GC Type");
        }

        for (int i = 0; i < 100000000; i++) {
            String object = new String("Object " + i);
            if (i % 100000 == 0) {
                System.gc();
            }
        }
        printGCLogs(gcOptions);
    }

    public static void printGCLogs(String gcOptions) {
        System.out.println("Using GC Options: " + gcOptions);
    }
}
