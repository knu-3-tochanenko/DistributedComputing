public class Garden {
    private boolean[][] garden = new boolean[Settings.ROWS][Settings.COLUMNS];

    Garden() {
        for (int i = 0; i < Settings.ROWS; i++)
            for (int j = 0; j < Settings.COLUMNS; j++)
                garden[i][j] = true;
    }

    public boolean getPlant(int x, int y) {
        return garden[x][y];
    }

    public void waterPlant(int x, int y) {
        garden[x][y] = true;
    }

    public void dryPlant(int x, int y) {
        garden[x][y] = false;
    }
}
