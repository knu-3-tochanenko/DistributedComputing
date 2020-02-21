import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordWriter implements Runnable {
    private File file;
    private RWLock lock;
    private AtomicBoolean isAlive;

    public RecordWriter(RWLock lock, AtomicBoolean isAlive) {
        this.file = new File(Settings.FILE_NAME);
        this.lock = lock;
        this.isAlive = isAlive;
    }

    @Override
    public void run() {
        String name, surname;
        long phone;
        while (isAlive.get()) {
            lock.modifyLock();
            name = Settings.getRandomName();
            surname = Settings.getRandomSurname();
            phone = Settings.getRandomPhone();

            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                pw.println(name + " " + surname + " " + phone);
                System.out.println(ANSI.paint(ANSI.BRIGHT_YELLOW, "Written to file : ")
                + ANSI.paint(ANSI.BRIGHT_CYAN, name + " " + surname + " " + phone));
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lock.modifyUnlock();

            try {
                Thread.sleep(Settings.SLEEP_TIME / 4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
