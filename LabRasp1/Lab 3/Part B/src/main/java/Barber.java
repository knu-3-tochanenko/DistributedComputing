import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Barber implements Runnable {

    private Semaphore barberChair;
    private AtomicBoolean isSleeping = new AtomicBoolean(true);

    public Barber(Semaphore barberChair) {
        this.barberChair = barberChair;
    }

    public void wakeUp() {
        if (isSleeping.get()) {
            isSleeping.set(false);
            barberChair.release();
            System.out.println(ANSI.BRIGHT_YELLOW + "Barber has waken up" + ANSI.RESET);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (isSleeping.get()) {
                System.out.println(ANSI.BRIGHT_RED + "Barber is sleeping... zzzz..." + ANSI.RESET);
                try {
                    Thread.sleep(Settings.SLEEP_TIME * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (barberChair.tryAcquire()) {
                    isSleeping.set(true);
                } else {
                    System.out.println(ANSI.BRIGHT_GREEN + "Barber is doing it's stuff" + ANSI.RESET);
                    barberChair.release();
                }
            }
        }
    }
}
