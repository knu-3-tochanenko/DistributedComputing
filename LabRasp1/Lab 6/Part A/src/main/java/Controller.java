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
                x.setStyle("-fx-background-color: green");
                M.set(1, matrix.getRowIndex(x), matrix.getColumnIndex(x));
            };

            x.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        }
    }

//    private boolean isRunning = false;
    private AtomicBoolean isAlive = new AtomicBoolean(false);

    @FXML
    public void startClick() {
        System.out.println("START");
//        isRunning = true;
        isAlive.set(true);
        Thread thread = new Thread(new LiveCore(M, matrix, isAlive));
        thread.start();
    }

    @FXML
    public void stopClick(ActionEvent actionEvent) {
        System.out.println("STOP");
        isAlive.set(false);
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
}
