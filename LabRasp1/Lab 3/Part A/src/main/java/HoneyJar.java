public class HoneyJar {
    private int volume;
    private int filled = 0;

    public HoneyJar(int volume) {
        this.volume = volume;
    }

    public synchronized void fill() {
        if (isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        filled++;
        System.out.println("Jar has " + filled + " sips of honey!");
    }

    public synchronized boolean isFull() {
        return filled == volume;
    }

    public synchronized void eat() {
        System.out.println("Jar is empty!");
        filled = 0;
        notifyAll();
    }
}
