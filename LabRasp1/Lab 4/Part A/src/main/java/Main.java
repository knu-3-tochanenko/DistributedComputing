import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        RWLock lock = new RWLock();

        try {
            PrintWriter pw = null;
            pw = new PrintWriter(new File(Settings.FILE_NAME));
            pw.print("");
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        AtomicBoolean nameAlive = new AtomicBoolean(true);
        AtomicBoolean numberAlive = new AtomicBoolean(true);
        AtomicBoolean removerAlive = new AtomicBoolean(true);
        AtomicBoolean writerAlive = new AtomicBoolean(true);

        Thread finderByName = new Thread(new RecordFinderByName(lock, nameAlive));
        Thread finderByNumber = new Thread(new RecordFinderByNumber(lock, numberAlive));
        Thread remover = new Thread(new RecordRemover(lock, removerAlive));
        Thread writer = new Thread(new RecordWriter(lock, writerAlive));

        writer.start();
        finderByName.start();
        finderByNumber.start();
        remover.start();

        try {
            Thread.sleep(Settings.SLEEP_TIME * 15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nameAlive.set(false);
        numberAlive.set(false);
        removerAlive.set(false);
        writerAlive.set(false);
    }
}
