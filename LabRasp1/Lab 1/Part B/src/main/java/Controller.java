import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
    private AtomicInteger position = new AtomicInteger(0);

    // SEMAPHORE set in `1` position if first thread has
    // access to position and in `2` if second.
    // If it is set in `0` position then no thread
    // is alive at the moment
    private AtomicInteger SEMAPHORE = new AtomicInteger(0);

    private Thread initSingleThread(int threadTarget, int semaphoreWorkingState) {
        return new Thread(() -> {
            while (SEMAPHORE.get() == semaphoreWorkingState) {
                Thread.yield();
                if (position.get() > threadTarget)
                    position.set(position.get() - 1);
                else if (position.get() < threadTarget)
                    position.set(position.get() + 1);
                else {
                    if (SEMAPHORE.compareAndSet(semaphoreWorkingState, 0))
                        Platform.runLater(() -> {
                            semaphore_state_text.setText("UNLOCKED");
                            buttonVisibility(false, false, true, true);
                        });
                }
                Platform.runLater(() -> {
                    synchronized (bar) {
                        position_text.setText(String.valueOf(position));
                        bar.setProgress(position.get() / 100.0);
                    }
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

    private void buttonVisibility(
            boolean run_first,
            boolean run_second,
            boolean kill_first,
            boolean kill_second) {
        button_run_first.setDisable(run_first);
        button_run_second.setDisable(run_second);
        button_kill_first.setDisable(kill_first);
        button_kill_second.setDisable(kill_second);
    }

    @FXML
    public void runFirst() {
        SEMAPHORE.set(1);

        Thread firstThread = initSingleThread(FIRST_THREAD_TARGET, 1);
        firstThread.setDaemon(true);

        semaphore_state_text.setText("LOCKED BY 1 THREAD");
        buttonVisibility(true, false, false, true);

        firstThread.setPriority(1);
        firstThread.start();
    }

    @FXML
    public void killFirst() {
        SEMAPHORE.set(0);

        semaphore_state_text.setText("UNLOCKED");
        buttonVisibility(false, false, true, true);
    }

    @FXML
    public void runSecond() {
        SEMAPHORE.set(2);

        Thread secondThread = initSingleThread(SECOND_THREAD_TARGET, 2);
        secondThread.setDaemon(true);

        semaphore_state_text.setText("LOCKED BY 2 THREAD");
        buttonVisibility(false, true, true, false);

        secondThread.setPriority(10);
        secondThread.start();
    }

    @FXML
    public void killSecond() {
        SEMAPHORE.set(0);

        semaphore_state_text.setText("UNLOCKED");
        buttonVisibility(false, false, true, true);
    }

}
