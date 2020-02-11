import java.util.concurrent.Semaphore;

public class Barber implements Runnable {

    private Semaphore sleeping;
    private Semaphore barberChair;
    private int totalCount = 0;

    public Barber(Semaphore sleeping, Semaphore barberChair) {
        this.sleeping = sleeping;
        this.barberChair = barberChair;
    }

    @Override
    public void run() {
        while (true) {
            if (sleeping.tryAcquire()) {
                System.out.println(ANSI.GREEN + "Barber is sleeping... zzzz..." + ANSI.RESET);
            } else {
                System.out.println(ANSI.BRIGHT_GREEN + "Barber is doing it's stuff" + ANSI.RESET);
                try {
                    Thread.sleep(Settings.SLEEP_TIME * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                totalCount++;
                barberChair.release();
                if (totalCount == Settings.CUSTOMER_COUNT) {
                    System.out.println(ANSI.RED + "My work is done. It's time to go home!!!" + ANSI.RESET);
                    break;
                }
            }
        }
    }
}
