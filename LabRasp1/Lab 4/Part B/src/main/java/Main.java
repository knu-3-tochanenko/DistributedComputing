import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) {
        try {
            PrintWriter pw = new PrintWriter(new File(Settings.FILE));
            pw.print("");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AtomicBoolean consoleAlive = new AtomicBoolean(true);
        AtomicBoolean fileAlive = new AtomicBoolean(true);
        AtomicBoolean gardenerAlive = new AtomicBoolean(true);
        AtomicBoolean natureAlive = new AtomicBoolean(true);

        Garden garden = new Garden();
        ReadWriteLock lock = new ReentrantReadWriteLock();

        Thread console = new Thread(new ConsoleLogger(garden, consoleAlive, lock));
        Thread file = new Thread(new FileLogger(garden, fileAlive, lock));
        Thread gardener = new Thread(new Gardener(garden, gardenerAlive, lock));
        Thread nature = new Thread(new Nature(garden, natureAlive, lock));

        nature.start();
        gardener.start();
        console.start();
        file.start();

        try {
            Thread.sleep(Settings.SLEEP_TIME * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        consoleAlive.set(false);
        gardenerAlive.set(false);
        natureAlive.set(false);
        try {
            Thread.sleep(Settings.SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileAlive.set(false);
    }
}
