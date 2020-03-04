public class Matrix {
    private int[][] M;

    Matrix() {
        M = new int[S.CELLS][];
        for (int i = 0; i < S.CELLS; i++) {
            M[i] = new int[S.CELLS];
            for (int j = 0; j < S.CELLS; j++)
                M[i][j] = 0;
        }
    }

    Matrix(Matrix matrix) {
        M = new int[S.CELLS][];
        for (int i = 0; i < S.CELLS; i++) {
            M[i] = new int[S.CELLS];
            for (int j = 0; j < S.CELLS; j++)
                M[i][j] = matrix.get(i, j);
        }
    }

    synchronized public void set(int val, int x, int y) {
        this.M[x][y] = val;
    }

    synchronized public int get(int x, int y) {
        return this.M[x][y];
    }

    synchronized void copy(Matrix matrix) {
        for (int i = 0; i < S.CELLS; i++) {
            for (int j = 0; j < S.CELLS; j++)
                M[i][j] = matrix.get(i, j);
        }
    }

    synchronized void clear() {
        for (int i = 0; i < S.CELLS; i++) {
            for (int j = 0; j < S.CELLS; j++)
                M[i][j] = 0;
        }
    }
}
