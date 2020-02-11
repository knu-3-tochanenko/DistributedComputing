public class Bee implements Runnable {
    private HoneyJar jar;
    private Bear bear;
    private MySemaphore semaphore;

    private int id;

    public Bee(HoneyJar jar, Bear bear, MySemaphore semaphore, int id) {
        this.jar = jar;
        this.bear = bear;
        this.semaphore = semaphore;
        this.id = id;
    }


    @Override
    public void run() {
        System.out.println("Bee " + id + " created");
        while (true) {
//            semaphore.take();
            try {
                Thread.sleep(Settings.SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Bee " + id + " filled jar with 1 sip");
            jar.fill();

            if (jar.isFull()) {
//                semaphore.release();
                System.out.println("Jar is full. Waking up the bear!");
                bear.wakeUp();
            } else {
//                semaphore.release();
            }

        }
    }
}
