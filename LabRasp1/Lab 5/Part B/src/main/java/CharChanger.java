import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class CharChanger implements Runnable {

    private int id;
    private StringBuffer string;
    private CyclicBarrier barrier;
    private AtomicBoolean isAlive;

    public CharChanger(int id, StringBuffer string, CyclicBarrier barrier, AtomicBoolean isAlive) {
        this.id = id;
        this.string = string;
        this.barrier = barrier;
        this.isAlive = isAlive;
    }

    @Override
    public void run() {
        while (isAlive.get()) {
            System.out.println(
                    ANSI.Red("Modifying string #")
                            + ANSI.Yellow(Integer.toString(id))
                            + ANSI.Red(" : ") + ANSI.Yellow(string.toString())
            );

            Random random = new Random(System.currentTimeMillis());
            int positionToChange = random.nextInt(S.STRING_LEN);
            char currentChar = string.charAt(positionToChange);
            switch (currentChar) {
                case 'A':
                    string.setCharAt(positionToChange, 'C');
                    break;
                case 'B':
                    string.setCharAt(positionToChange, 'D');
                    break;
                case 'C':
                    string.setCharAt(positionToChange, 'A');
                    break;
                case 'D':
                    string.setCharAt(positionToChange, 'B');
                    break;
                default:
                    System.out.println(ANSI.Blue("This shouldn't happen!"));
            }

            try {
                Thread.sleep(S.SLEEP_TIME);
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
