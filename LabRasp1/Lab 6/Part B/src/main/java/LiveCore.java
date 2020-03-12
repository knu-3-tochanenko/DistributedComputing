import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class LiveCore implements Runnable {
    private Matrix M, sub;
    private AtomicBoolean isAlive;

    private CyclicBarrier barrier;
    private Semaphore semaphore;

    private int species;

    LiveCore(Matrix m, Matrix sub, CyclicBarrier barrier, Semaphore semaphore, AtomicBoolean isAlive, int species) {
        M = m;
        this.sub = sub;
        this.barrier = barrier;
        this.semaphore = semaphore;
        this.isAlive = isAlive;
        this.species = species;
    }

    @Override
    public void run() {
        while (isAlive.get()) {
            for (int i = 0; i < S.CELLS; i++) {
                for (int j = 0; j < S.CELLS; j++) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!isAlive.get())
                        return;
                    changeIfNeeded(i, j, species);
                    semaphore.release();
                }
            }
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeIfNeeded(int x, int y, int species) {
        int sum = 0;
        for (int i = Math.max(x - 1, 0); i < Math.min(x + 2, S.CELLS); i++) {
            for (int j = Math.max(y - 1, 0); j < Math.min(y + 2, S.CELLS); j++) {
                if (i == x && j == y) continue;
                if (M.get(i, j) == species)
                    sum++;
            }
        }

        if (M.get(x, y) == species && (sum < 2 || sum > 3))
            sub.set(0, x, y);
        if (M.get(x, y) == 0 && sum == 3)
            sub.set(species, x, y);
    }
}
