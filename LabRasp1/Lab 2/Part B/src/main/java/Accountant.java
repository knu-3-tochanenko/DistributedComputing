import java.util.concurrent.atomic.AtomicBoolean;

public class Accountant extends Thread {
    private int sum = 0;
    private AtomicBoolean isWorking;
    private AtomicBoolean loaderWorking;
    private ItemQueue queue;

    Accountant(ItemQueue queue, AtomicBoolean loaderWorking, AtomicBoolean isWorking) {
        this.queue = queue;
        this.loaderWorking = loaderWorking;
        this.isWorking = isWorking;
    }

    @Override
    public void run() {
        Item item;
        while (isWorking.get() || !queue.isEmpty()) {
            if (!loaderWorking.get()) {
                isWorking.set(false);
            }
            item = queue.get();
            try {
                sleep(Settings.DELAY);
            } catch (InterruptedException e) {
                this.interrupt();
            }
            sum += item.getPrice();
            System.out.println("Accountant got item #" + item.getCode() +
                    " : $" + item.getPrice() + ". Total sum by far is $" + sum);
        }
        System.out.println("We stolen items for total of $" + sum + "!");
    }
}
