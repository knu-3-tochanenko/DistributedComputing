import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    private Matrix M;
    private Matrix sub;

    @FXML
    private GridPane matrix;

    @FXML
    private RadioButton check_green;

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
                if (check_green.isSelected()) {
                    if (M.get(GridPane.getRowIndex(x), GridPane.getColumnIndex(x)) != 1) {
                        x.setStyle("-fx-background-color: green");
                        M.set(1, GridPane.getRowIndex(x), GridPane.getColumnIndex(x));
                    } else {
                        x.setStyle("-fx-background-color: pink");
                        M.set(0, GridPane.getRowIndex(x), GridPane.getColumnIndex(x));
                    }
                } else {
                    if (M.get(GridPane.getRowIndex(x), GridPane.getColumnIndex(x)) != 2) {
                        x.setStyle("-fx-background-color: red");
                        M.set(2, GridPane.getRowIndex(x), GridPane.getColumnIndex(x));
                    } else {
                        x.setStyle("-fx-background-color: pink");
                        M.set(0, GridPane.getRowIndex(x), GridPane.getColumnIndex(x));
                    }
                }
            };

            x.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        }
    }

    private AtomicBoolean isAliveGreen = new AtomicBoolean(true);
    private AtomicBoolean isAliveRed = new AtomicBoolean(true);

    @FXML
    public void startClick() {
        sub.copy(M);

        isAliveGreen.set(true);
        isAliveRed.set(true);

        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            Platform.runLater(this::redrawField);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            M.copy(sub);
        });

        Semaphore semaphore = new Semaphore(1);

        new Thread(new LiveCore(M, sub, barrier, semaphore, isAliveGreen, 1)).start();
        new Thread(new LiveCore(M, sub, barrier, semaphore, isAliveRed, 2)).start();
    }

    @FXML
    public void stopClick() {
        isAliveGreen.set(false);
        isAliveRed.set(false);
    }

    @FXML
    public void resetClick() {
        M.clear();
        drawField();
    }

    private AtomicReference<Node> node = new AtomicReference<>();

    private void drawField() {
        for (int i = 0; i < S.CELLS; i++)
            for (int j = 0; j < S.CELLS; j++) {
                node.set(S.getNodeByRowColumnIndex(i, j, matrix));
                node.get().setStyle("-fx-background-color: " + getColor(i, j));
            }
    }

    private void redrawField() {
        for (int i = 0; i < S.CELLS; i++)
            for (int j = 0; j < S.CELLS; j++) {
                if (M.get(i, j) != sub.get(i, j)) {
                    M.set(sub.get(i, j), i, j);
                    node.set(S.getNodeByRowColumnIndex(i, j, matrix));
                    node.get().setStyle("-fx-background-color: " + getColor(i, j));
                }
            }
    }

    private String getColor(int x, int y) {
        if (sub.get(x, y) == 1)
            return "green";
        if (sub.get(x, y) == 2)
            return "red";
        return "pink";
    }
}
