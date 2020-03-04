import javafx.scene.layout.GridPane;

import java.util.concurrent.atomic.AtomicBoolean;

public class LiveCore implements Runnable {
    private Matrix M;
    private GridPane matrix;
    private AtomicBoolean isAlive;

    LiveCore(Matrix m, GridPane matrix, AtomicBoolean isAlive) {
        M = m;
        this.matrix = matrix;
        this.isAlive = isAlive;
    }

    @Override
    public void run() {
        Matrix sub = new Matrix(M);
        while (isAlive.get()) {
            // TODO logic
        }

    }
}
