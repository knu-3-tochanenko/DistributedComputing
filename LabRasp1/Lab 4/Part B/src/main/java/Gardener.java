import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class Gardener implements Runnable {
    private Garden garden;
    private AtomicBoolean isAlive;
    private Lock lock;

    public Gardener(Garden garden, AtomicBoolean isAlive, ReadWriteLock lock) {
        this.garden = garden;
        this.isAlive = isAlive;
        this.lock = lock.writeLock();
    }

    @Override
    public void run() {
        while (isAlive.get()) {
            lock.lock();

            System.out.println(ANSI.paint(ANSI.BRIGHT_YELLOW, "Gardener began to work"));
            for (int i = 0; i < Settings.ROWS; i++)
                for (int j = 0; j < Settings.COLUMNS; j++)
                    if (!garden.getPlant(i, j)) {
                        garden.waterPlant(i, j);
                        System.out.println(ANSI.paint(ANSI.BRIGHT_YELLOW, "Gardener watered " + i + " - " + j));
                    }

            lock.unlock();

            try {
                Thread.sleep(Settings.SLEEP_TIME * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
