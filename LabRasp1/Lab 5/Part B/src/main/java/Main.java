import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        StringBuffer[] strings = new StringBuffer[S.THREAD_COUNT];
        AtomicBoolean[] isAlives = new AtomicBoolean[S.THREAD_COUNT];

        CyclicBarrier barrier = new CyclicBarrier(4, () -> {
            System.out.println(ANSI.Cyan("BARRIER REACHED"));
            AtomicBoolean res = new AtomicBoolean(false);
            Thread checker = new Thread(new CharChecker(strings, res));
            checker.start();
            try {
                checker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (res.get()) {
                System.out.println(ANSI.Green("End of program!"));
                for (int i = 0; i < S.THREAD_COUNT; i++) {
                    System.out.println(ANSI.Yellow(strings[i].toString()));
                    isAlives[i].set(false);
                }
                System.exit(0);
            } else {
                System.out.println(ANSI.Cyan("CONTINUING PROGRAM..."));
                for (int i = 0; i < S.THREAD_COUNT; i++)
                    System.out.println(ANSI.Yellow(strings[i].toString()));
            }
        });

        for (int i = 0; i < S.THREAD_COUNT; i++) {
            strings[i] = StringGenerator.generate();
            isAlives[i] = new AtomicBoolean(true);
            new Thread(new CharChanger(i, strings[i], barrier, isAlives[i])).start();
        }

    }
}
