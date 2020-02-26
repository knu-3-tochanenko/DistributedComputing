public class GameRunner extends Thread {
    private GamePanel panel;

    GameRunner(GamePanel newPanel) {
        panel = newPanel;
    }

    @Override
    public void run() {
        if (panel.tank == null) {
            panel.tank = new Tank(panel.mainFrame, panel);
            panel.tank.start();
        }

        while (!isInterrupted()) {
            if (panel.nyanCats.size() < panel.getMaxDucks()) {
                NyanCat nyanCat = new NyanCat(panel.width, panel.height, panel);
                panel.nyanCats.add(nyanCat);
                nyanCat.start();
            }
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
