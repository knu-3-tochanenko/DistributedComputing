import java.util.concurrent.ForkJoinPool;

public class StripesDivider {
    private final double[][] A;
    private final double[][] B;

    private double[][] C;

    private final int threadsNumb;

    public StripesDivider(double[][] A, double[][] B, int threadsNumb) {
        this.A = A;
        this.B = B;
        this.C = new double[A.length][B[0].length];

        for (int i = 0; i < C.length; i++) {
            C[i] = new double[B[0].length];
        }

        this.threadsNumb = threadsNumb;
    }

    public double[][] calculateProduct() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadsNumb);

        for (int i = 0; i < threadsNumb; i++) {
            forkJoinPool.invoke(new MatrixProduct(A, B, C, i, threadsNumb));
        }

        forkJoinPool.shutdown();
        return C;
    }
}