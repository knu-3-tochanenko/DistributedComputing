import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    private Matrix M;
    private Matrix sub;

    @FXML
    private GridPane matrix;

    @FXML
    public void initialize() {
        M = new Matrix();
        sub = new Matrix(M);
        ObservableList<Node> children = matrix.getChildren();

        for (int i = 0; i < S.CELLS; i++) {
            for (int j = 0; j < S.CELLS; j++) {
                Label label = new Label();
                label.setPrefHeight(11);
                label.setPrefWidth(11);
                label.setStyle("-fx-background-color: pink");
                GridPane.setRowIndex(label, j);
                GridPane.setColumnIndex(label, i);
                matrix.getChildren().add(label);
            }
        }

        for (Node x : children) {
            EventHandler<MouseEvent> eventHandler = e -> {
                if (M.get(GridPane.getRowIndex(x), GridPane.getColumnIndex(x)) == 1) {
                    x.setStyle("-fx-background-color: pink");
                    M.set(0, GridPane.getRowIndex(x), GridPane.getColumnIndex(x));
                } else {
                    x.setStyle("-fx-background-color: black");
                    M.set(1, GridPane.getRowIndex(x), GridPane.getColumnIndex(x));
                }
            };

            x.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        }
    }

    private AtomicBoolean[] isAlive;

    @FXML
    public void startClick() {
        int len = (S.CELLS / S.LEN) * (S.CELLS / S.LEN);

        System.out.println(len);
        int sLen = (S.CELLS / S.LEN);
        isAlive = new AtomicBoolean[len];
        for (int i = 0; i < len; i++) {
            isAlive[i] = new AtomicBoolean(true);
        }



        CyclicBarrier barrier = new CyclicBarrier(len, () -> {
            Runnable runnable = this::redrawField;
            Platform.runLater(() -> {
                runnable.run();

            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        for (int i = 0; i < sLen; i++)
            for (int j = 0; j < sLen; j++) {
                new Thread(new LiveCore(M, sub, barrier, matrix, isAlive[i * sLen + j],
                        j * S.LEN, (j + 1) * S.LEN,
                        i * S.LEN, (i + 1) * S.LEN)).start();
            }
    }

    @FXML
    public void stopClick() {
        int len = (S.CELLS / S.LEN) * (S.CELLS / S.LEN);
        for (int i = 0; i < len; i++) {
            isAlive[i].set(false);
        }
    }

    @FXML
    public void resetClick() {
        M.clear();
        drawField();
    }

    AtomicReference<Node> node = new AtomicReference<>();

    private void drawField() {
        for (int i = 0; i < S.CELLS; i++)
            for (int j = 0; j < S.CELLS; j++) {
                node.set(S.getNodeByRowColumnIndex(i, j, matrix));
                node.get().setStyle("-fx-background-color: " + (M.get(i, j) == 1 ? "black" : "pink"));

            }
    }

    private void redrawField() {
        for (int i = 0; i < S.CELLS; i++)
            for (int j = 0; j < S.CELLS; j++) {
                if (M.get(i, j) != sub.get(i, j)) {
                    M.set(sub.get(i, j), i, j);
                    node.set(S.getNodeByRowColumnIndex(i, j, matrix));
                    node.get().setStyle("-fx-background-color: " + (M.get(i, j) == 1 ? "black" : "pink"));
                }

            }
    }
}
