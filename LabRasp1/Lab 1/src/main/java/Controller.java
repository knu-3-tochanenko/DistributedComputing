import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;

public class Controller {
    @FXML
    private Button button;

    @FXML
    private Spinner<Integer> starting_spinner;

    @FXML
    private Spinner<Integer> first_spinner;

    @FXML
    private Spinner<Integer> second_spinner;

    @FXML
    private ProgressBar bar;

    private boolean isRunning = false;

    @FXML
    public void onClick() {
        if (!isRunning) {
            button.setText("STOP");
            isRunning = true;
        }
        else {
            button.setText("RUN");
            isRunning = false;
        }

    }

}
