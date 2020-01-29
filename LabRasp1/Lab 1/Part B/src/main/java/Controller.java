import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;

import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
    @FXML
    private Button button_run_first;

    @FXML
    private Button button_run_second;

    @FXML
    private Button button_kill_first;

    @FXML
    private Button button_kill_second;

    @FXML
    private Label semaphore_state_text;

    @FXML
    private ProgressBar bar;

    @FXML
    private Label position_text;

    private static final int FIRST_THREAD_TARGET = 10;
    private static final int SECOND_THREAD_TARGET = 90;
    private static final int THREAD_SPEED = 100;
    private int position = 0;
    private Thread firstThread, secondThread;
    private AtomicBoolean firstAlive = new AtomicBoolean(true);
    private AtomicBoolean secondAlive = new AtomicBoolean(false);

    // SEMAPHORE set in `1` position if first thread has
    // access to position and in `2` if second.
    // If it is set in `0` position then no thread
    // is alive at the moment
    private int SEMAPHORE = 0;

    private Thread initSingleThread(int threadTarget, AtomicBoolean isAlive, int semaphoreWorkingState) {
        return new Thread(() -> {
            while (isAlive.get() && SEMAPHORE == semaphoreWorkingState) {
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

    @FXML
    private void initialize() {
        button_kill_first.setDisable(true);
        button_kill_second.setDisable(true);
    }

    @FXML
    public void runFirst() {
        SEMAPHORE = 1;

        firstThread = initSingleThread(FIRST_THREAD_TARGET, firstAlive, 1);
        firstThread.setDaemon(true);

        semaphore_state_text.setText("LOCKED BY 1 THREAD");
        button_run_first.setDisable(true);
        button_run_second.setDisable(false);
        button_kill_first.setDisable(false);
        button_kill_second.setDisable(true);

        firstAlive.set(true);
        firstThread.start();
    }

    @FXML
    public void killFirst() {
        SEMAPHORE = 0;

        firstAlive.set(false);

        semaphore_state_text.setText("UNLOCKED");
        button_run_first.setDisable(false);
        button_run_second.setDisable(false);
        button_kill_first.setDisable(true);
        button_kill_second.setDisable(true);
    }

    @FXML
    public void runSecond() {
        SEMAPHORE = 2;

        secondThread = initSingleThread(SECOND_THREAD_TARGET, secondAlive, 2);
        secondThread.setDaemon(true);

        semaphore_state_text.setText("LOCKED BY 2 THREAD");
        button_run_first.setDisable(false);
        button_run_second.setDisable(true);
        button_kill_first.setDisable(true);
        button_kill_second.setDisable(false);

        secondAlive.set(true);
        secondThread.start();
    }

    @FXML
    public void killSecond() {
        SEMAPHORE = 0;

        secondAlive.set(false);

        semaphore_state_text.setText("UNLOCKED");
        button_run_first.setDisable(false);
        button_run_second.setDisable(false);
        button_kill_first.setDisable(true);
        button_kill_second.setDisable(true);
    }

}
