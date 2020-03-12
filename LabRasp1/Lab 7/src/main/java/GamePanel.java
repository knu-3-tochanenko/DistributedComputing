import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GamePanel extends JPanel {
    int width;
    int height;

    private int coins = 0;
    private int maxCoins = 0;
    private JLabel score;
    private JLabel bestScore;
    private ImageIcon background = new ImageIcon(getClass().getResource("background.png"));
    private final int minimumDucks = 2;
    private static final int maxBullets = 5;
    private int maxDucks = minimumDucks;

    ConcurrentLinkedQueue<NyanCat> nyanCats = new ConcurrentLinkedQueue<>();

    Tank tank = null;
    MainFrame mainFrame;

    GamePanel(MainFrame mainFrame) {
        setBackground(Color.WHITE);
        this.mainFrame = mainFrame;
        width = mainFrame.getWidth();
        height = mainFrame.getHeight();

        // field editor
        setLayout(null);
        setSize(width, height);


        score = setupText("Score: 0", 22, Color.WHITE, 200, 100, 10, height - 630);
        this.add(score);

        bestScore = setupText("Best Score: 0", 22, Color.WHITE, 200, 100, 10, height - 600);
        this.add(bestScore);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("sight.png"));
        Cursor c =
                toolkit.createCustomCursor(image, new Point(getX() + 14, getY() + 14), "breech-sight");
        setCursor(c);

        GameMouseAdapter myMouseAdapter = new GameMouseAdapter(this);
        addMouseListener(myMouseAdapter);

        GameRunner game = new GameRunner(this);
        game.start();
    }

    private JLabel setupText(String text, int fontSize, Color color, int width, int height, int x, int y) {
        JLabel label = new JLabel(text);
        label.setVisible(true);
        label.setFont(new Font("Iosevka Bold Extended", Font.PLAIN, fontSize));
        label.setForeground(color);
        label.setSize(width, height);
        label.setLocation(x, y);
        return label;
    }

    synchronized int getMaxDucks() {
        return maxDucks;
    }

    synchronized int getMaxBullets() {
        return maxBullets;
    }

    synchronized void changedCoins(int d) {
        coins += d;
        if (coins < 0) coins = 0;
        if (maxCoins < coins) maxCoins = coins;

        int coinsLog2 = (int) Math.floor(Math.log(coins) / Math.log(2.0)),
                anotherCoins = (int) Math.pow(2.0, coinsLog2);

        if (anotherCoins == coins) maxDucks = coinsLog2;
        if (maxDucks < minimumDucks) maxDucks = minimumDucks;

        score.setText("Score: " + coins);
        bestScore.setText("Best Score: " + maxCoins);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, width, height, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(width, height);
    }
}
