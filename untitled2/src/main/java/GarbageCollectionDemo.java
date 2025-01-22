public class GarbageCollectionDemo {

    public static void main(String[] args) {
        System.out.println("Starting the program...");

        // Create objects inside a block to limit their scope
        {
            LargeObject obj1 = new LargeObject(1);
            LargeObject obj2 = new LargeObject(2);

            obj1 = null;
            obj2 = null;

            System.out.println("Objects created and set to null.");
        }

        // Suggest the JVM to run the garbage collector
        System.gc();

        System.out.println("Garbage collection suggested. Simulating other operations...");
        try {
            Thread.sleep(3000); // Pause to observe GC in action
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Program ending...");
    }
}

class LargeObject {
    private int id;

    public LargeObject(int id) {
        this.id = id;
        System.out.println("LargeObject " + id + " created.");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("LargeObject " + id + " is being garbage collected.");
        super.finalize();
    }
}
