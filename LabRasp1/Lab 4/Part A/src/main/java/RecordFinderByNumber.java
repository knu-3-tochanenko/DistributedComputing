import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordFinderByNumber implements Runnable {
    private File file;
    private RWLock lock;
    private AtomicBoolean isAlive = new AtomicBoolean(true);

    synchronized public void stop() {
        isAlive.set(false);
    }

    public RecordFinderByNumber(File file, RWLock lock) {
        this.file = file;
        this.lock = lock;
    }

    @Override
    public void run() {

    }
}
