import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMouseAdapter extends MouseAdapter {
    private GamePanel panel;
    private static final int maxRadius = 100;

    GameMouseAdapter(GamePanel newPanel) {
        panel = newPanel;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    @Override
    public void mouseReleased(MouseEvent e) {
        int rad = 0;
        if (e.isAltDown()) rad = maxRadius;

        int x = e.getX();
        int y = e.getY();
        for (NyanCat nyanCat : panel.nyanCats) {
            synchronized (nyanCat) {
                if (x >= nyanCat.x - rad
                        && x <= nyanCat.x + nyanCat.sizeX + rad
                        && y >= nyanCat.y - rad
                        && y <= nyanCat.y + nyanCat.sizeY + rad) nyanCat.interrupt();
            }
        }
    }
}
