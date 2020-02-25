import javax.swing.*;
import java.awt.*;

public class Bullet extends Thread {
    private int x;
    private int y;

    private static final int dy = 5;
    private static final int dx = 3;
    private final int sizeX = 70;
    private final int sizeY = 60;
    boolean side;

    private GamePanel panel;
    private JLabel bulletLabel;
    private Hunter hunter;

    Bullet(GamePanel panel, Hunter hunter, int x, int y, boolean side) {
        this.panel = panel;
        this.hunter = hunter;
        this.x = x;
        this.y = y;
        this.bulletLabel = new JLabel(new ImageIcon(getClass().getResource(side ? "fishLR.png" : "fishRL.png")));
        bulletLabel.setSize(new Dimension(sizeX, sizeY));
        bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);
        this.side = side;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    @Override
    public void run() {
        hunter.addBullet(1);
        panel.add(bulletLabel);

        while (!isInterrupted()) {
            if (y < 0) break;
            y -= dy;
            if (side)
                x += dx;
            else
                x -= dx;

            bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);

            for (Duck duck : panel.ducks) {
                synchronized (duck) {
                    if (duck.x < x && x < duck.x + duck.sizeX && duck.y < y && y < duck.y + duck.sizeY) {
                        duck.interrupt();
                        this.interrupt();
                        break;
                    }
                }
            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }

        panel.remove(bulletLabel);
        panel.repaint();
        hunter.addBullet(-1);
    }
}
