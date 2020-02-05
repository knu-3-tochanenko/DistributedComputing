import java.util.LinkedList;
import java.util.Queue;

public class ItemQueue {
    private volatile int size;
    private Queue<Item> queue = new LinkedList<>();

    ItemQueue(int size) {
        this.size = size;
    }

    public synchronized void add(Item element) {
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

    public synchronized Item get() {
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
}
