import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(4, () -> {

        });
    }
}
