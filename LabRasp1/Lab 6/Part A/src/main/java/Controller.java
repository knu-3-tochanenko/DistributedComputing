import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
    private Matrix M;

    @FXML
    private GridPane matrix;

    @FXML
    private Button btn_start;

    @FXML
    private Button btn_stop;

    @FXML
    private Button btn_reset;

    @FXML
    private Slider slider_speed;

    @FXML
    private Label text_speed;

    @FXML
    public void initialize() {
        M = new Matrix();
        ObservableList<Node> children = matrix.getChildren();

        for (int i = 0; i < S.CELLS; i++) {
            for (int j = 0; j < S.CELLS; j++) {
                Label label = new Label();
                label.setPrefHeight(11);
                label.setPrefWidth(11);
                label.setStyle("-fx-background-color: pink");
                matrix.setRowIndex(label, j);
                matrix.setColumnIndex(label, i);
                matrix.getChildren().add(label);
            }
        }

        for (Node x : children) {
            EventHandler<MouseEvent> eventHandler = e -> {
//                System.out.println(x);
                if (M.get(matrix.getRowIndex(x), matrix.getColumnIndex(x)) == 1) {
                    x.setStyle("-fx-background-color: pink");
                    M.set(0, matrix.getRowIndex(x), matrix.getColumnIndex(x));
                } else {
                    x.setStyle("-fx-background-color: black");
                    M.set(1, matrix.getRowIndex(x), matrix.getColumnIndex(x));
                }
            };

            x.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        }
    }

    //    private boolean isRunning = false;
    private AtomicBoolean[] isAlive;

    @FXML
    public void startClick() {
        System.out.println("START");

        int len = (S.CELLS / S.LEN) * (S.CELLS / S.LEN);
        int sLen = (S.CELLS / S.LEN);
        isAlive = new AtomicBoolean[len];
        for (int i = 0; i < len; i++) {
            isAlive[i] = new AtomicBoolean(true);

        }

        Random rand = new Random();

        Matrix sub = new Matrix(M);

        CyclicBarrier barrier = new CyclicBarrier(len, () -> {
            M.copy(sub);
            Platform.runLater(() -> {
                for (int i = 0; i < S.CELLS; i++)
                    for (int j = 0; j < S.CELLS; j++ ) {
                        Node node = getNodeByRowColumnIndex(i, j, matrix);
                        node.setStyle("-fx-background-color: " + (M.get(i, j) == 1 ? "black" : "pink"));
                    }
            });
        });


        for (int i = 0; i < sLen; i++)
            for (int j = 0; j < sLen; j++) {
                new Thread(new LiveCore(M, sub, barrier, matrix, isAlive[i * sLen + j],
                        j * S.LEN, (j + 1) * S.LEN,
                        i * S.LEN, (i + 1) * S.LEN)).start();
            }
    }

    @FXML
    public void stopClick(ActionEvent actionEvent) {
        System.out.println("STOP");
        int len = (S.CELLS / S.LEN) * (S.CELLS / S.LEN);
        for (int i = 0; i < len; i++) {
            isAlive[i].set(false);
        }
    }

    @FXML
    public void resetClick(ActionEvent actionEvent) {
        System.out.println("RESET");
        M.clear();
        for (int i = 0; i < S.CELLS; i++) {
            for (int j = 0; j < S.CELLS; j++) {
                Label label = new Label();
                label.setPrefHeight(11);
                label.setPrefWidth(11);
                label.setStyle("-fx-background-color: pink");
                matrix.setRowIndex(label, j);
                matrix.setColumnIndex(label, i);
                matrix.getChildren().add(label);
            }
        }
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
