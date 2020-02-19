import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoldiersNormalizer implements Runnable {
    private ArrayList<Boolean> list;
    private int l, r;
    private Barrier barrier;
    private AtomicBoolean isChanged = new AtomicBoolean(true);

    public SoldiersNormalizer(
            ArrayList<Boolean> list,
            int l, int r,
            Barrier barrier//,
//            AtomicBoolean isChanged
    ) {
        this.list = list;
        this.l = l;
        this.r = r;
        this.barrier = barrier;
//        this.isChanged = isChanged;
    }

    @Override
    public void run() {
        while (isChanged.get()) {
//            System.out.print("Normalizing ["
//                    + ANSI.Yellow(Integer.toString(l))
//                    + "; "
//            + ANSI.Yellow(Integer.toString(r)) + "]");

//            System.out.print("Normalizing ");
//            for (int i = l; i < r; i++)
//                if (list.get(i))
//                    System.out.print('>');
//                else
//                    System.out.print('<');
//            System.out.println();
            try {
                Thread.sleep(Settings.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isChanged.set(false);
            for (int i = l; i < r; i++) {
//                if (list.get(i) != list.get(i + 1)) {
//                    list.set(i, !list.get(i));
//                    list.set(i + 1, !list.get(i + 1));
//                    System.out.println(ANSI.Blue("Soldiers ")
//                            + ANSI.Yellow(Integer.toString(i))
//                            + ANSI.Blue(" and ")
//                            + ANSI.Yellow(Integer.toString(i + 1))
//                            + ANSI.Blue(" turned over IN ["
//                            + ANSI.Yellow(Integer.toString(l))
//                            + ANSI.Blue("; ")
//                            + ANSI.Yellow(Integer.toString(r))
//                            + ANSI.Blue("].")));
//                    isChanged.set(true);
//                }
                if (!list.get(i)) {
                    list.set(i, true);
                    System.out.println("Turned soldier " + i);
                    isChanged.set(true);
                }
            }

            if (!isChanged.get()) {
                System.out.println(ANSI.Purple("List in bounds [")
                        + ANSI.Yellow(Integer.toString(l))
                        + ANSI.Purple("; ")
                        + ANSI.Yellow(Integer.toString(r))
                        + ANSI.Purple("] is normalized!"));
            }

            try {
                Thread.sleep(Settings.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            barrier.await();
        }
    }
}
