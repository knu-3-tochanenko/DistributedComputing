import java.util.Random;

public class MatrixGenerator {
    private int rowSize;
    private int columnSize;
    private double maxValue;

    Random random;

    MatrixGenerator(int size, double maxValue) {
        this(size, size, maxValue);
    }

    MatrixGenerator(int rowSize, int columnSize, double maxValue) {
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.maxValue = maxValue;

        random = new Random();
    }

    double[][] generate() {
        double[][] matrix = new double[rowSize][columnSize];

        for (int i = 0; i < rowSize; i++) {
            matrix[i] = new double[columnSize];
            for (int j = 0; j < columnSize; j++) {
                matrix[i][j] = maxValue * random.nextDouble();
            }
        }

        return matrix;
    }
}
