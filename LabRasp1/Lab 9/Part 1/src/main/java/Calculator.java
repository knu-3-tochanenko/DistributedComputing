import java.util.LinkedList;
import java.util.List;

public class Calculator {
    private HTMLBuilder htmlBuilder;
    private List<Integer> sizes;

    Calculator() {
        sizes = new LinkedList<>();

        htmlBuilder = new HTMLBuilder();
        htmlBuilder.build();
        htmlBuilder.createTable();
    }

    void addTask(Integer size) {
        sizes.add(size);
    }

    void run() {
        double sequentialTime;
        double time, acceleration;
        List<Pair<Double, Double>> results;

        for (Integer i : sizes) {
            System.out.println("Calculating matrix size of " + i + " ...");
            results = new LinkedList<>();
            sequentialTime = ((double) calculate(i, 1)) / 1000.0;
            System.out.println(ANSI.Yellow("\t1 core\t") + sequentialTime);

            time = ((double) calculate(i, 2)) / 1000.0;
            acceleration = sequentialTime / time;
            results.add(new Pair<>(time, acceleration));
            System.out.println(ANSI.Yellow("\t2 cores\t") + time);


            time = ((double) calculate(i, 4)) / 1000.0;
            acceleration = sequentialTime / time;
            results.add(new Pair<>(time, acceleration));
            System.out.println(ANSI.Yellow("\t4 cores\t") + time + "\n");


            htmlBuilder.addResult(i, sequentialTime, results);
        }

        finish();
    }

    private Long calculate(int size, int threadsNumber) {
        MatrixGenerator matrixGenerator = new MatrixGenerator(size, 100);
        double[][] A = matrixGenerator.generate();
        double[][] B = matrixGenerator.generate();

        StripesDivider stripesSchema = new StripesDivider(A, B, threadsNumber);
        long startTime = System.currentTimeMillis();

        stripesSchema.calculateProduct();

        return System.currentTimeMillis() - startTime;
    }

    private void finish() {
        htmlBuilder.finishTable();
        htmlBuilder.finish();
    }
}
