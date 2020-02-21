import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class FileLogger implements Runnable {
    private Garden garden;
    private AtomicBoolean isAlive;
    private Lock lock;
    private File file;

    public FileLogger(Garden garden, AtomicBoolean isAlive, ReadWriteLock lock) {
        this.garden = garden;
        this.isAlive = isAlive;
        this.lock = lock.readLock();
        this.file = new File(Settings.FILE);
    }

    @Override
    public void run() {
        while (isAlive.get()) {
            lock.lock();

            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                pw.println("---------------------");
                for (int i = 0; i < Settings.ROWS; i++) {
                    for (int j = 0; j < Settings.COLUMNS; j++) {
                        if (garden.getPlant(i, j))
                            pw.print("O");
                        else
                            pw.print("-");
                    }
                    pw.println("");
                }
                pw.close();
                System.out.println(ANSI.paint(ANSI.BRIGHT_CYAN, "LOG SAVED TO FILE"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            lock.unlock();

            try {
                Thread.sleep(Settings.SLEEP_TIME * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
