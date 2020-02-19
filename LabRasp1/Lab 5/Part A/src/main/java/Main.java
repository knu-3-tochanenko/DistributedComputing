import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ArrayList<Boolean> list = generateArray();
        int blockCount = (int) Math.ceil(Settings.RECRUIT_COUNT * 1.0 / Settings.BLOCK_SIZE);
        Thread[] thread = new Thread[blockCount];
        Barrier barrier = new Barrier(blockCount, () -> {
            System.out.println(ANSI.Cyan("Barrier reached!"));

            for (int i = 0; i < Settings.RECRUIT_COUNT; i++)
                if (list.get(i))
                    System.out.print('>');
                else
                    System.out.print('<');
            System.out.println();

            try {
                Thread.sleep(Settings.SLEEP_TIME * 15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < blockCount - 1; i++) {
            thread[i] = new Thread(new SoldiersNormalizer(list,
                    i * Settings.BLOCK_SIZE,
                    (i + 1) * Settings.BLOCK_SIZE, barrier));
            thread[i].start();
        }

        thread[blockCount - 1] = new Thread(new SoldiersNormalizer(list,
                (blockCount - 1) * Settings.BLOCK_SIZE,
                Settings.RECRUIT_COUNT, barrier));
        thread[blockCount - 1].start();

        for (int  i = 0; i < blockCount; i++) {
            try {
                thread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static ArrayList<Boolean> generateArray() {
        Random random = new Random(System.currentTimeMillis());
        ArrayList<Boolean> list = new ArrayList<>();
        for (int i = 0; i < Settings.RECRUIT_COUNT; i++)
            list.add(random.nextBoolean());
        return list;
    }
}
