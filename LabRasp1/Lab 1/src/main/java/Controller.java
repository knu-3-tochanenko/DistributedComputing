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
    private static final int firstThreadTarget = 10;
    private static final int secondThreadTarget = 90;
    private int position = 0;
    private Thread firstThread, secondThread;
    private AtomicBoolean firstAlive = new AtomicBoolean(true);
    private AtomicBoolean secondAlive = new AtomicBoolean(false);

    private void initThreads() {
        firstThread = new Thread(() -> {
            while (firstAlive.get()) {
                Thread.yield();
                if (position > firstThreadTarget)
                    position--;
                else if (position < firstThreadTarget)
                    position++;
                Platform.runLater(() -> {
                    position_text.setText(String.valueOf(position));
                    bar.setProgress(position / 100.0);
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        firstThread.setPriority(first_spinner.getValue());
        firstThread.setDaemon(true);

        secondThread = new Thread(() -> {
            while (secondAlive.get()) {
                Thread.yield();
                if (position > secondThreadTarget)
                    position--;
                else if (position < secondThreadTarget)
                    position++;
                Platform.runLater(() -> {
                    position_text.setText(String.valueOf(position));
                    bar.setProgress(position / 100.0);
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        secondThread.setPriority(second_spinner.getValue());
        secondThread.setDaemon(true);
    }

    public void startThreads() {
        position = starting_spinner.getValue();
        bar.setProgress(position / 100.0);
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
            startThreads();
            isRunning = true;
        } else {
            button.setText("RUN");
            killThreads();
            isRunning = false;
        }
    }

}
