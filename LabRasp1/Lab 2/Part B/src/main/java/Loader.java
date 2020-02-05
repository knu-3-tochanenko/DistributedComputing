import java.util.concurrent.atomic.AtomicBoolean;

public class Loader extends Thread {
    private AtomicBoolean isWorking;
    private AtomicBoolean thiefWorking;
    private AtomicBoolean accountantWorking;
    private ItemQueue fromThief;
    private ItemQueue toAccountant;

    Loader(ItemQueue fromThief, ItemQueue toAccountant,
           AtomicBoolean thiefWorking, AtomicBoolean isWorking, AtomicBoolean accountantWorking) {
        this.fromThief = fromThief;
        this.toAccountant = toAccountant;
        this.isWorking = isWorking;
        this.thiefWorking = thiefWorking;
        this.accountantWorking = accountantWorking;
    }

    @Override
    public void run() {
        Item item;
        while (isWorking.get() || !fromThief.isEmpty()) {
            item = fromThief.get();
            System.out.println("Loader loaded item " + ANSI.BRIGHT_YELLOW + "#" + item.getCode() +
                    ANSI.RESET + " : " + ANSI.BRIGHT_GREEN + "$" + item.getPrice() + ANSI.RESET);
            try {
                sleep(Settings.DELAY);
            } catch (InterruptedException e) {
                this.interrupt();
            }
            toAccountant.add(item);
            if (fromThief.isEmpty() && !thiefWorking.get()) {
                accountantWorking.set(false);
            }
        }
    }
}
