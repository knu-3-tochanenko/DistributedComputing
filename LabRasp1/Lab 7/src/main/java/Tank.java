import javax.swing.*;
import java.awt.*;

public class Tank extends Thread {
    private JLabel hunterLabel;
    private GamePanel panel;

    private int x;
    private int y;

    static final int sizeX = 200;
    private static final int sizeY = 130;

    private static final int dx = 20;
    private int width;
    private volatile int countBullet = 0;

    private int side = 1;

    private boolean keyLeft = false;
    private boolean keyRight = false;

    Tank(MainFrame mainFrame, GamePanel panel) {
        this.width = panel.width;
        this.panel = panel;
        x = panel.width / 2;
        y = panel.height - sizeY - 30;

        hunterLabel = new JLabel(new ImageIcon(getClass().getResource("hunterRL.png")));
        hunterLabel.setSize(new Dimension(sizeX, sizeY));
        hunterLabel.setLocation(x, y);
        hunterLabel.setVisible(true);
        panel.add(hunterLabel);

        HunterKeyListener keyListener = new HunterKeyListener(panel);
        mainFrame.addKeyListener(keyListener);
    }

    synchronized void addBullet(int num) {
        countBullet += num;
    }

    synchronized int getCountBullet() {
        return countBullet;
    }

    synchronized int getSide() {
        return side;
    }

    synchronized void setKeys(boolean newKeyLeft, boolean newKeyRight) {
        keyLeft = newKeyLeft;
        keyRight = newKeyRight;
    }

    synchronized int getX() {
        return x;
    }

    synchronized int getY() {
        return y;
    }

    synchronized void setSide(int newSide) {
        side = newSide;
    }

    synchronized void setIcon(Icon icon) {
        hunterLabel.setIcon(icon);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (keyLeft && x - dx >= 0) x -= dx;
            else if (keyRight && x + dx + sizeX <= width) x += dx;

            hunterLabel.setLocation(x, y);

            try {
                sleep(30);
            } catch (InterruptedException e) {
                break;
            }
        }

        panel.remove(hunterLabel);
    }
}
