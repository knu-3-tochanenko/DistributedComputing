import java.util.concurrent.Semaphore;

public class BarberShop implements Runnable {
    private Semaphore customerQueue;
    private Semaphore barberChair;
    private Semaphore sleeping;

    public BarberShop(Semaphore customerQueue, Semaphore barberChair, Semaphore sleeping) {
        this.customerQueue = customerQueue;
        this.barberChair = barberChair;
        this.sleeping = sleeping;
    }


    @Override
    public void run() {
        Thread barber = new Thread(new Barber(sleeping, barberChair));
        barber.start();

        for (int i = 0; i < Settings.CUSTOMER_COUNT; i++) {
            try {
                Thread.sleep(Settings.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(new Customer(i, customerQueue, barberChair, sleeping));
            thread.start();
        }
    }
}
