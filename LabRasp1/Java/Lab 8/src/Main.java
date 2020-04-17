public class Main {
    public static void main(String[] args) {
        int[] sizes = {100, 500, 1000};

        for (int matSize : sizes) {
            SimpleMatrix.calculate(args, matSize);
            StringMatrix.calculate(args, matSize);
            FoxMatrix.calculate(args, matSize);
            CannonMatrix.calculate(args, matSize);
        }
    }
}
