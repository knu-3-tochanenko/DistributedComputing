public class Pair<T extends Number, R extends Number> {
    private T x;
    private R y;

    public R getY() {
        return y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public void setY(R y) {
        this.y = y;
    }

    public Pair(T x, R y) {
        this.x = x;
        this.y = y;
    }
}
