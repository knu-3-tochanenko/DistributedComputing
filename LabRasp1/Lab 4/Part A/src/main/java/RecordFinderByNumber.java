import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordFinderByNumber implements Runnable {
    private File file;
    private RWLock lock;
    private AtomicBoolean isAlive;

    public RecordFinderByNumber(RWLock lock, AtomicBoolean isAlive) {
        this.file = new File(Settings.FILE_NAME);
        this.lock = lock;
        this.isAlive = isAlive;
    }

    @Override
    public void run() {
        while (isAlive.get()) {
            lock.readLock();
            long findByPhone = Settings.getRandomPhone();
            System.out.println(ANSI.paint(ANSI.BRIGHT_GREEN, "Searching for : "
                    + ANSI.paint(ANSI.BRIGHT_CYAN, Long.toString(findByPhone))));
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                boolean isFound = false;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts[2].equals(Long.toString(findByPhone))) {
                        System.out.println(ANSI.paint(ANSI.BRIGHT_GREEN, "FOUND : "
                                + ANSI.paint(ANSI.BRIGHT_CYAN, line)));
                        isFound = true;
                    }
                }
                if (!isFound) {
                    System.out.println(ANSI.paint(ANSI.BRIGHT_RED, "No records found for ")
                            + ANSI.paint(ANSI.BRIGHT_CYAN, Long.toString(findByPhone)));
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            lock.readUnlock();

            try {
                Thread.sleep(Settings.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
