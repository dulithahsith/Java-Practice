public class MemoryLeakExample2 {

    // Class A has a reference to class B
    public static class AAA {
        private BBB b;

        public void setB(BBB b) {
            this.b = b;
        }

        public BBB getB() {
            return b;
        }
    }

    // Class B has a reference to class A (creating a cyclic reference)
    public static class BBB {
        private AAA a;

        public void setA(AAA a) {
            this.a = a;
        }

        public AAA getA() {
            return a;
        }
    }

    public static void main(String[] args) {
        // Create instances of A and B
        AAA a = new AAA();
        BBB b = new BBB();

        // Set up cyclic references
        a.setB(b);
        b.setA(a);

        // Simulate a memory leak by keeping references to objects
        // Without breaking the cycle, these objects won't be collected
        while (true) {
            // The cyclic reference will prevent garbage collection
            // since both A and B hold references to each other.
        }
    }
}
