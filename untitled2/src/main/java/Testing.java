public class Testing {
    public static void main(String[] args){

        for (int i=0; i<100000;i++){
            new Thread(()->{
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName());

            }).start();
        }

    }
}
