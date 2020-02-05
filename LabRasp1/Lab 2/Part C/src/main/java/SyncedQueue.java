import java.util.LinkedList;
import java.util.Queue;

public class SyncedQueue {
    private volatile int size;
    private Queue<Monk> queue = new LinkedList<>();

    SyncedQueue(int size) {
        this.size = size;
    }

    public synchronized int getSize() {
        return queue.size();
    }

    public synchronized void add(Monk element) {
        if (size <= queue.size()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        queue.add(element);
        notifyAll();
    }

    public synchronized Monk get() {
        if (queue.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();
        return queue.remove();
    }

    public synchronized boolean isEmpty() {
        return queue.size() == 0;
    }

    public synchronized void clear() {
        queue.clear();
    }

    public synchronized void copy(SyncedQueue queue) {
        this.clear();
        while (!queue.isEmpty())
            this.queue.add(queue.get());
        queue.clear();
    }
}