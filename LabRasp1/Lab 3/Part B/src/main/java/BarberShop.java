import java.util.Random;
import java.util.concurrent.Semaphore;

public class BarberShop implements Runnable {
    private Semaphore customerQueue;
    private Semaphore barberChair;

    public BarberShop(Semaphore customerQueue, Semaphore barberChair) {
        this.customerQueue = customerQueue;
        this.barberChair = barberChair;
    }


    @Override
    public void run() {
        Barber barber = new Barber(barberChair);
        Thread barberThread = new Thread(barber);
        barberThread.start();

        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < Settings.CUSTOMER_COUNT; i++) {
            try {
                Thread.sleep(Settings.SLEEP_TIME * (random.nextInt(10) * random.nextInt(7) + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(new Customer(i, customerQueue, barberChair, barber));
            thread.start();
        }
    }
}
