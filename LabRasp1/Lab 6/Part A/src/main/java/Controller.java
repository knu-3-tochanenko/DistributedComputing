import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Controller {
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
        ObservableList<Node> children = matrix.getChildren();

        for (int i = 0; i < 52; i++) {
            for (int j = 0; j < 52; j++) {
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
            };

            x.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        }

//        EventHandler<MouseEvent> eventHandler = e -> {
//            text_speed.setStyle("-fx-text-fill: blue");
//        };
//
//        text_speed.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

//        text_speed.setOnMouseClicked(event -> text_speed.setStyle("-fx-text-fill: red"));
//        new Thread( () -> {
//            matrix.
//        }).start();
    }
}
