import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class MatrixProduct extends RecursiveAction {
    private final double[][] A;
    private final double[][] B;

    private double[][] C;

    private final int rowStart;
    private final int rowEnd;

    private final int columnStart;
    private final int columnEnd;

    private int i = 0;
    private int threadsNumber = 0;

    public MatrixProduct(final double[][] A, final double[][] B, double[][] C, int i, int threadsNumber) {
        this(A, B, C, 0, A.length, 0, B.length);
        this.i = i;
        this.threadsNumber = threadsNumber;
    }

    public MatrixProduct(final double[][] A, final double[][] B, double[][] C, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        this.A = A;
        this.B = B;
        this.C = C;

        this.rowStart = rowStart;
        this.rowEnd = rowEnd;

        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
    }

    @Override
    protected void compute() {
        if (threadsNumber > 0) {
            List<MatrixProduct> tasks = new ArrayList<>();
            int taskSize = (int) Math.round((double) A.length / (double) threadsNumber);
            int rowStart, rowEnd;
            int columnStart, columnEnd;

            for (int j = 0; j < threadsNumber; j++) {
                rowStart = j * taskSize;
                rowEnd = (j == (threadsNumber - 1)) ? A.length : (j + 1) * taskSize;
                columnStart = ((i + j) % threadsNumber) * taskSize;
                columnEnd = (columnStart / taskSize == threadsNumber - 1) ? A.length : (columnStart + taskSize);

                MatrixProduct task = new MatrixProduct(A, B, C, rowStart, rowEnd, columnStart, columnEnd);
                tasks.add(task);
            }

            RecursiveAction.invokeAll(tasks);
        } else {
            for (int i = rowStart; i < rowEnd; i++) {
                for (int j = columnStart; j < columnEnd; j++) {
                    C[i][j] = calculateEntry(i, j);
                }
            }
        }
    }

    private double calculateEntry(int i, int j) {
        double result = 0.0;
        for (int k = 0; k < B.length; k++) {
            result += A[i][k] * B[k][j];
        }
        return result;
    }
}
