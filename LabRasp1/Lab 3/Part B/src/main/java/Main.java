import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore customerQueue = new Semaphore(Settings.CUSTOMER_COUNT);
        Semaphore barberChair = new Semaphore(1);
        Semaphore sleeping = new Semaphore(1);

        new Thread(new BarberShop(customerQueue, barberChair, sleeping)).start();
    }
}
