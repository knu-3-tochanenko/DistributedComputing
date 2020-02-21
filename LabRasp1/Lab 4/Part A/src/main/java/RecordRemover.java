import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordRemover implements Runnable {
    private File file;
    private RWLock lock;
    private AtomicBoolean isAlive;

    public RecordRemover(RWLock lock, AtomicBoolean isAlive) {
        this.file = new File(Settings.FILE_NAME);
        this.lock = lock;
        this.isAlive = isAlive;
    }

    @Override
    public void run() {
        while (isAlive.get()) {
            lock.modifyLock();
            String nameToDelete = Settings.getRandomName();
            File tempFile = new File(Settings.TEMP_FILE_NAME);
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String parts[] = line.split(" ");
                    if (!parts[1].equals(nameToDelete)) {
                        pw.println(line);
                    } else {
                        System.out.println(ANSI.paint(ANSI.BRIGHT_RED, "DELETED : "
                                + ANSI.paint(ANSI.BRIGHT_CYAN, line)));
                    }
                }
                pw.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!file.delete()) System.out.println(ANSI.paint(ANSI.BRIGHT_RED, "ERROR : Can't delete file"));
            if (!tempFile.renameTo(file)) System.out.println(ANSI.paint(ANSI.BRIGHT_RED, "ERROR : Can't rename file"));

            lock.modifyUnlock();

            try {
                Thread.sleep(Settings.SLEEP_TIME * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
