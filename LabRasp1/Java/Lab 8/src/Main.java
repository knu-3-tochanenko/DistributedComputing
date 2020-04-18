import algorithm.CannonMatrix;
import algorithm.FoxMatrix;
import algorithm.SimpleMatrix;
import algorithm.StringMatrix;

public class Main {
    public static void main(String[] args) {
//        int[] sizes = { 24 * 5, 24 * 21, 24 * 42 };
        int[] sizes = { 24 * 21, 24 * 42, 24 * 125 };

        for (int matSize : sizes) {
            SimpleMatrix.calculate(args, matSize);
            StringMatrix.calculate(args, matSize);
            FoxMatrix.calculate(args, matSize);
            CannonMatrix.calculate(args, matSize);
        }
    }
}
