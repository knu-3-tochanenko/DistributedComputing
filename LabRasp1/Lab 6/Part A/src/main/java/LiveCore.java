import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
            System.out.println("RUNNING...");
            sub.copy(M);
            for (int i = 0; i < S.CELLS; i++)
                for (int j = 0; j < S.CELLS; j++) {
                    if (!isAlive.get())
                        return;
                    sub.set(aliveCell(i, j) ? 1 : 0, i, j);
                    final int iL = i;
                    final int jL = j;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        Node node = getNodeByRowColumnIndex(iL, jL, matrix);
                        node.setStyle("-fx-background-color:" + (aliveCell(iL, jL) ? " green" : " pink"));
                    });
                }

            M.copy(sub);
        }
    }

    private boolean aliveCell(int x, int y) {
        int sum = 0;
        for (int i = Math.max(x - 1, 0); i < Math.min(x + 2, S.CELLS); i++) {
            for (int j = Math.max(y - 1, 0); j < Math.min(y + 2, S.CELLS); j++) {
                if (i == x && j == y) continue;
                sum += M.get(i, j);
            }
        }
        System.out.println("Sum around" + x + " - " + y + " = " + sum);
        if (sum == 3)
            return true;
        if (M.get(x, y) == 1) {
            if (sum < 2)
                return false;
            return sum <= 3;
        }
        return false;
    }

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}
