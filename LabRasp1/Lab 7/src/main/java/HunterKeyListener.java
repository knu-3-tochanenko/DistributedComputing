import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HunterKeyListener implements KeyListener {
    private GamePanel panel;

    HunterKeyListener(GamePanel newPanel) {
        panel = newPanel;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (panel != null) {

            if (keyEvent.getKeyCode() == 32 && panel.tank.getCountBullet() < panel.getMaxBullets()) {
                Bullet bullet;
                if (panel.tank.getSide() == 1)
                    bullet = new Bullet(
                            panel,
                            panel.tank,
                            panel.tank.getX() + 60,
                            panel.tank.getY(),
                            false
                    );
                else
                    bullet = new Bullet(
                            panel,
                            panel.tank,
                            panel.tank.getX() + Tank.sizeX - 60,
                            panel.tank.getY(),
                            true
                    );
                bullet.start();
            }

            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                panel.tank.setKeys(true, false);
                if (panel.tank.getX() > 0) {
                    if (panel.tank.getSide() != 1) {
                        panel.tank.setIcon(newIcon("hunterRL.png"));
                        panel.tank.setSide(1);
                    }
                }
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                panel.tank.setKeys(false, true);
                if (panel.tank.getX() < panel.width - Tank.sizeX) {
                    if (panel.tank.getSide() != 2) {
                        panel.tank.setIcon(newIcon("hunterLR.png"));
                        panel.tank.setSide(2);
                    }
                }
            }
        }
    }

    private ImageIcon newIcon(String name) {
        return new ImageIcon(panel.getClass().getResource(name));
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if ((keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
                || (keyEvent.getKeyCode() == KeyEvent.VK_LEFT))
            panel.tank.setKeys(false, false);
    }
}
