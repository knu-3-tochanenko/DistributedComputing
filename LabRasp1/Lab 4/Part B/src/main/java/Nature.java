import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class Nature implements Runnable {
    private Garden garden;
    private AtomicBoolean isAlive;
    private Lock lock;

    public Nature(Garden garden, AtomicBoolean isAlive, ReadWriteLock lock) {
        this.garden = garden;
        this.isAlive = isAlive;
        this.lock = lock.writeLock();
    }

    @Override
    public void run() {
        while (isAlive.get()) {
            lock.lock();

            Random random = new Random(System.currentTimeMillis());
            int x, y;
            System.out.println(ANSI.paint(ANSI.BRIGHT_PURPLE, "Nature began to work"));
            for (int i = 0; i < Settings.DRIED; i++) {
                x = random.nextInt(Settings.ROWS);
                y = random.nextInt(Settings.COLUMNS);

                if (garden.getPlant(x, y)) {
                    garden.dryPlant(x, y);
                    System.out.println(ANSI.paint(ANSI.BRIGHT_PURPLE, "Nature dried " + x + " - " + y));
                } else {
                    i--;
                }
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
