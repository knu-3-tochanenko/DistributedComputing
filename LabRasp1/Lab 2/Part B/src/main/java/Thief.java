import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Thief extends Thread {
    private AtomicBoolean isWorking;
    AtomicBoolean loaderWorking;
    private ItemQueue queue;
    private Random random = new Random(System.currentTimeMillis());
    private int stolenItems = 0;

    Thief(ItemQueue queue, AtomicBoolean isWorking, AtomicBoolean loaderWorking) {
        this.queue = queue;
        this.isWorking = isWorking;
        this.loaderWorking = loaderWorking;
    }

    @Override
    public void run() {
        while (isWorking.get()) {
            if (stolenItems >= Settings.ALL_ELEMENTS) {
                System.out.println(ANSI.BRIGHT_RED + "Items have ended!" + ANSI.RESET);
                isWorking.set(false);
                loaderWorking.set(false);
            }
            Item newItem = new Item(random.nextInt(1000),
                    random.nextInt(50) * 100 + 99);
            queue.add(newItem);
            stolenItems++;
            System.out.println("Thief put item " + ANSI.BRIGHT_CYAN + "#" + newItem.getCode() +
                    ANSI.RESET + " : " + ANSI.BRIGHT_GREEN + "$" + newItem.getPrice() + ANSI.RESET);
            try {
                sleep(Settings.DELAY);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
