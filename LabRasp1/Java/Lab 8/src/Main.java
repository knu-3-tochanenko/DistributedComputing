import algorithm.CannonMatrix;
import algorithm.FoxMatrix;
import algorithm.SimpleMatrix;
import algorithm.StringMatrix;

public class Main {
    public static void main(String[] args) {
//        int[] sizes = { 24 * 5, 24 * 21, 24 * 42 };   // Small
        int[] sizes = { 24 * 21, 24 * 42, 24 * 125 };   // Big

        for (int matrixSize : sizes) {
            SimpleMatrix.calculate(args, matrixSize);
            StringMatrix.calculate(args, matrixSize);
            FoxMatrix.calculate(args, matrixSize);
            CannonMatrix.calculate(args, matrixSize);
        }
    }
}
