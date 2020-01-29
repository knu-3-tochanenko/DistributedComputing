import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;

import java.util.concurrent.atomic.AtomicBoolean;

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

    @FXML
    private Label position_text;

    private boolean isRunning = false;
    private static final int FIRST_THREAD_TARGET = 10;
    private static final int SECOND_THREAD_TARGET = 90;
    private static final int THREAD_SPEED = 5;
    private int position = 0;
    private Thread firstThread, secondThread;
    private AtomicBoolean firstAlive = new AtomicBoolean(true);
    private AtomicBoolean secondAlive = new AtomicBoolean(false);

    private Thread initSingleThread(int threadTarget) {
        return new Thread(() -> {
            while (firstAlive.get()) {
                Thread.yield();
                if (position > threadTarget)
                    position--;
                else if (position < threadTarget)
                    position++;
                Platform.runLater(() -> {
                    position_text.setText(String.valueOf(position));
                    bar.setProgress(position / 100.0);
                });
                try {
                    Thread.sleep(THREAD_SPEED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initThreads() {
        firstThread = initSingleThread(FIRST_THREAD_TARGET);
        firstThread.setPriority(first_spinner.getValue());
        firstThread.setDaemon(true);

        secondThread = initSingleThread(SECOND_THREAD_TARGET);
        secondThread.setPriority(second_spinner.getValue());
        secondThread.setDaemon(true);
    }

    public void startThreads() {
        initThreads();
        firstAlive.set(true);
        secondAlive.set(true);
        firstThread.start();
        secondThread.start();
    }

    public void killThreads() {
        firstAlive.set(false);
        secondAlive.set(false);
    }

    @FXML
    public void onClick() {
        if (!isRunning) {
            button.setText("STOP");

            position = starting_spinner.getValue();
            bar.setProgress(position / 100.0);

            starting_spinner.setDisable(true);
            first_spinner.setDisable(true);
            second_spinner.setDisable(true);

            startThreads();
            isRunning = true;
        } else {
            button.setText("RUN");

            starting_spinner.setDisable(false);
            first_spinner.setDisable(false);
            second_spinner.setDisable(false);

            killThreads();
            isRunning = false;
        }
    }

}
