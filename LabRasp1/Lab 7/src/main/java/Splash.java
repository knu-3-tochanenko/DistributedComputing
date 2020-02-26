import javax.swing.*;
import java.awt.*;

public class Splash extends Thread {
    private JLabel splash = new JLabel(new ImageIcon(getClass().getResource("splash.png")));
    private GamePanel panel;
    int x, y;
    int sizeX = 110;
    int sizeY = 110;

    Splash(GamePanel panel, int x, int y) {
        this.panel = panel;
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {
        splash.setSize(new Dimension(sizeX, sizeY));
        splash.setLocation(x, y);
        panel.add(splash);
        panel.repaint();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            interrupt();
        }
        panel.remove(splash);
        panel.repaint();

    }
}
