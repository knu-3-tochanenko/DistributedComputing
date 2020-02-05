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
            System.out.println("Accountant got item " + ANSI.BRIGHT_YELLOW + "#" + item.getCode() +
                    ANSI.RESET + " : " + ANSI.BRIGHT_GREEN + "$" + item.getPrice() + ANSI.RESET
            + ". Total sum by far is " + ANSI.BRIGHT_GREEN + "$" + sum + ANSI.RESET);
        }
        System.out.println(ANSI.BRIGHT_PURPLE + "We stolen items for total of " +
                ANSI.BRIGHT_GREEN + "$" + sum + ANSI.BRIGHT_PURPLE + "!" + ANSI.RESET);
    }
}
