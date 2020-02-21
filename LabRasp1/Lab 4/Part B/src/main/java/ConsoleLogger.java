import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class ConsoleLogger implements Runnable {
    private Garden garden;
    private AtomicBoolean isAlive;
    private Lock lock;

    public ConsoleLogger(Garden garden, AtomicBoolean isAlive, ReadWriteLock lock) {
        this.garden = garden;
        this.isAlive = isAlive;
        this.lock = lock.readLock();
    }

    @Override
    public void run() {
        while (isAlive.get()) {
            lock.lock();

            for (int i = 0; i < Settings.ROWS; i++) {
                for (int j = 0; j < Settings.COLUMNS; j++) {
                    if (garden.getPlant(i, j))
                        System.out.print(ANSI.paint(ANSI.BRIGHT_GREEN, "O"));
                    else
                        System.out.print(ANSI.paint(ANSI.BRIGHT_RED, "-"));
                }
                System.out.println();
            }

            lock.unlock();

            try {
                Thread.sleep(Settings.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
