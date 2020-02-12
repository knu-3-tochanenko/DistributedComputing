public class MySemaphore {
    private boolean locked = false;

    public synchronized void take()  {
        while (locked) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        locked = true;
    }

    public synchronized void release() {
        if (locked) notify();
        locked = false;
    }

}
