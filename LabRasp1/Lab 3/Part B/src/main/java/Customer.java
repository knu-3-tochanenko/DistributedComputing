import java.util.concurrent.Semaphore;

public class Customer implements Runnable {
    private Semaphore customerQueue;
    private Semaphore barberChair;
    private Semaphore sleeping;

    private int id;

    public Customer(int id, Semaphore customerQueue, Semaphore barberChair, Semaphore sleeping) {
        this.id = id;
        this.customerQueue = customerQueue;
        this.barberChair = barberChair;
        this.sleeping = sleeping;
    }

    @Override
    public void run() {
        if (customerQueue.tryAcquire()) {
            while (!barberChair.tryAcquire()) {
                System.out.println("Customer " + ANSI.BRIGHT_PURPLE + id + ANSI.RESET + " is waiting...");
                try {
                    Thread.sleep(Settings.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Client " + ANSI.BRIGHT_PURPLE + id + ANSI.RESET + " is now looking fabulous!");
            sleeping.release();
            customerQueue.release();
        } else {
            System.out.println("This should't happen!");
        }
    }
}
