public class RWLock {
    private boolean locked = false;
    private int readers = 0;

    synchronized void readLock() {
        while (locked) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        readers++;
    }

    synchronized void readUnlock() {
        readers--;
        if (readers == 0)
            notifyAll();
    }

    synchronized void writeLock() {
        while (readers > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        locked = true;
    }

    synchronized void writeUnlock() {
        locked = false;
        notifyAll();
    }
}
