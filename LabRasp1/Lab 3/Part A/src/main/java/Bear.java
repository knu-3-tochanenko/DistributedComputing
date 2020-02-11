public class Bear implements Runnable {
    private HoneyJar jar;
    private MySemaphore semaphore;
    private volatile boolean isAlive = false;

    public Bear(HoneyJar jar, MySemaphore semaphore) {
        this.jar = jar;
        this.semaphore = semaphore;
    }

    public synchronized void wakeUp() {
        isAlive = true;
        notify();
    }

    @Override
    public void run() {
        System.out.println("bear created");
        while (true) {
            synchronized (this) {
                while (!isAlive) {
                    System.out.println("Bear waits...");
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

//                semaphore.take();
                jar.eat();
                try {
                    Thread.sleep(Settings.SLEEP_INTERVAL * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Bear has eaten all the honey!");
                isAlive = false;
//                semaphore.release();

            }

        }
    }
}
