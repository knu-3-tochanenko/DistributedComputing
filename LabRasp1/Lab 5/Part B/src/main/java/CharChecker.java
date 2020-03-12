import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class CharChecker implements Runnable {
    private StringBuffer[] strings;
    private AtomicBoolean result;

    public CharChecker(StringBuffer[] strings, AtomicBoolean result) {
        this.strings = strings;
        this.result = result;
    }

    @Override
    public void run() {
        System.out.println(ANSI.Purple("Counting amount of A and B"));
        long[] countA = new long[S.THREAD_COUNT];
        long[] countB = new long[S.THREAD_COUNT];
        long[] sums = new long[S.THREAD_COUNT];
        for (int i = 0; i < S.THREAD_COUNT; i++) {
            countA[i] = strings[i].chars().filter(ch -> ch == 'A').count();
            countB[i] = strings[i].chars().filter(ch -> ch == 'B').count();
            sums[i] = countA[i] + countB[i];
            System.out.println(
                    ANSI.Purple("String #") + ANSI.Yellow(Integer.toString(i))
                            + ANSI.Purple(" - A: ") + ANSI.Yellow(Long.toString(countA[i]))
                            + ANSI.Purple("; B: ") + ANSI.Yellow(Long.toString(countB[i]))
                            + ANSI.Purple("; SUM: ") + ANSI.Yellow(Long.toString(sums[i]))
            );

        }

        Arrays.sort(sums);

        System.out.print(ANSI.Purple("Sorted sums of A and B : "));
        for (int i = 0; i < S.THREAD_COUNT; i++)
            System.out.print(ANSI.Yellow(Long.toString(sums[i])) + " ");
        System.out.println("");

        result.set(false);
        if (sums[0] == sums[S.THREAD_COUNT - 1])
            result.set(true);
        else if (sums[0] == sums[S.THREAD_COUNT - 2]
                || sums[1] == sums[S.THREAD_COUNT - 1])
            result.set(true);

        System.out.println(ANSI.Purple("Result : ")
                + ANSI.Yellow(Boolean.toString(result.get())));

        try {
            Thread.sleep(S.SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
