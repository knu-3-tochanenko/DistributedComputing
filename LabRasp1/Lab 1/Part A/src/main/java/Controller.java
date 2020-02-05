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
    private AtomicInteger position = new AtomicInteger(0);
    private Thread firstThread, secondThread;
    private AtomicBoolean firstAlive = new AtomicBoolean(true);
    private AtomicBoolean secondAlive = new AtomicBoolean(false);

    @FXML
    public void initialize() {
        initThreads();
        first_spinner.valueProperty().addListener((obs, oldValue, newValue) -> firstThread.setPriority(newValue));
        second_spinner.valueProperty().addListener((obs, oldValue, newValue) -> secondThread.setPriority(newValue));
    }

    private Thread initSingleThread(int threadTarget, AtomicBoolean isAlive) {
        return new Thread(() -> {
            while (isAlive.get()) {
                Thread.yield();
                if (position.get() > threadTarget)
                    position.set(position.get() - 1);
                else if (position.get() < threadTarget)
                    position.set(position.get() + 1);
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

    private void initThreads() {
        firstThread = initSingleThread(FIRST_THREAD_TARGET, firstAlive);
        firstThread.setPriority(first_spinner.getValue());
        firstThread.setDaemon(true);

        secondThread = initSingleThread(SECOND_THREAD_TARGET, secondAlive);
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

        try {
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClick() {
        if (!isRunning) {
            button.setText("STOP");

            position.set(starting_spinner.getValue());
            bar.setProgress(position.get() / 100.0);

            starting_spinner.setDisable(true);

            startThreads();
            isRunning = true;
        } else {
            button.setText("RUN");

            starting_spinner.setDisable(false);

            killThreads();
            isRunning = false;
        }
    }

}
