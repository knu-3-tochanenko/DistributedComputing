import java.util.concurrent.Semaphore;

public class Customer implements Runnable {
    private Semaphore customerQueue;
    private Semaphore barberChair;
    private Barber barber;

    private int id;

    public Customer(int id, Semaphore customerQueue, Semaphore barberChair, Barber barber) {
        this.id = id;
        this.customerQueue = customerQueue;
        this.barberChair = barberChair;
        this.barber = barber;
    }

    @Override
    public void run() {
        if (customerQueue.tryAcquire()) {
            while (!barberChair.tryAcquire()) {
                barber.wakeUp();
                System.out.println("Customer " + ANSI.BRIGHT_PURPLE +
                        id + ANSI.RESET + " is waiting...");
                try {
                    Thread.sleep(Settings.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(ANSI.BRIGHT_GREEN + "Client " + ANSI.BRIGHT_PURPLE +
                    id + ANSI.BRIGHT_GREEN + " is now looking fabulous!" + ANSI.RESET);
            customerQueue.release();
        } else {
            System.out.println("This should't happen!");
        }
    }
}
