import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class LiveCore implements Runnable {
    private Matrix M, sub;
    private GridPane matrix;
    private AtomicBoolean isAlive;

    private CyclicBarrier barrier;

    private int l, r, t, b;
    String color;

    private String[] colors = {"darksalmon", "aquamarine", "lightgreen", "lightgrey", "navajowhite", "yellow", "plum"};

    LiveCore(Matrix m, Matrix sub, CyclicBarrier barrier, GridPane matrix, AtomicBoolean isAlive, int l, int r, int t, int b) {
        M = m;
        this.sub = sub;
        this.barrier = barrier;
        this.matrix = matrix;
        this.isAlive = isAlive;
        this.l = l;
        this.r = r;
        this.t = t;
        this.b = b;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (isAlive.get()) {
//            System.out.println("WORKING IN COLOR " + color);
            color = colors[random.nextInt(7)];
            for (int i = t; i < b; i++)
                for (int j = l; j < r; j++) {
                    if (!isAlive.get())
                        return;
                    sub.set(aliveCell(i, j) ? 1 : 0, i, j);
                    final int iL = i;
                    final int jL = j;
                    if (S.COLORED) {
                        Platform.runLater(() -> {
                            Node node = getNodeByRowColumnIndex(iL, jL, matrix);
                            node.setStyle("-fx-background-color: " + (M.get(iL, jL) == 1 ? "black" : color));
                        });
                    }
                    try {
                        Thread.sleep(5 * (S.COLORED ? 2 : 1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
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
