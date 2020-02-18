import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordFinderByName implements Runnable {
    private File file;
    private RWLock lock;
    private AtomicBoolean isAlive = new AtomicBoolean(true);

    synchronized public void stop() {
        isAlive.set(false);
    }

    public RecordFinderByName(File file) {
        this.file = file;
    }

    @Override
    public void run() {

    }
}
