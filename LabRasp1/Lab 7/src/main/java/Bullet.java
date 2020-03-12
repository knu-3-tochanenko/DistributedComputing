import javax.swing.*;
import java.awt.*;

public class Bullet extends Thread {
    private int x;
    private int y;

    private static final int dy = 10;
    private static final int dx = 6;
    private final int sizeX = 70;
    private final int sizeY = 60;
    boolean side;

    private GamePanel panel;
    private JLabel bulletLabel;
    private Tank tank;

    Bullet(GamePanel panel, Tank tank, int x, int y, boolean side) {
        this.panel = panel;
        this.tank = tank;
        this.x = x;
        this.y = y;
        this.bulletLabel = new JLabel(new ImageIcon(getClass().getResource(side ? "rainLR.png" : "rainRL.png")));
        bulletLabel.setSize(new Dimension(sizeX, sizeY));
        bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);
        this.side = side;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    @Override
    public void run() {
        tank.addBullet(1);
        panel.add(bulletLabel);

        while (!isInterrupted()) {
            if (y < 0) break;
            y -= dy;
            if (side)
                x += dx;
            else
                x -= dx;

            bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);

            for (NyanCat nyanCat : panel.nyanCats) {
                synchronized (nyanCat) {
                    if (nyanCat.x < x && x < nyanCat.x + nyanCat.sizeX && nyanCat.y < y && y < nyanCat.y + nyanCat.sizeY) {
                        nyanCat.interrupt();
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
        tank.addBullet(-1);
    }
}
