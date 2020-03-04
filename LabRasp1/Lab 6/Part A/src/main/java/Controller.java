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

public class Controller {
    private int[][] M;

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
        M = new int[S.CELLS][];
        ObservableList<Node> children = matrix.getChildren();

        for (int i = 0; i < S.CELLS; i++) {
            M[i] = new int[S.CELLS];
            for (int j = 0; j < S.CELLS; j++) {
                M[i][j] = 0;
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
                System.out.println(x);
                x.setStyle("-fx-background-color: green");
                M[matrix.getRowIndex(x)][matrix.getColumnIndex(x)] = 1;
            };

            x.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandler);
        }
    }

    private boolean isRunning = false;

    @FXML
    public void startClick() {
        isRunning = true;

    }

    @FXML
    public void stopClick(ActionEvent actionEvent) {

    }

    @FXML
    public void resetClick(ActionEvent actionEvent) {

    }
}
