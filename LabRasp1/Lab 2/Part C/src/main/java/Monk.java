public class Monk {
    private String monastery;
    private int qi;

    Monk(int qi, String monastery) {
        this.qi = qi;
        this.monastery = monastery;
    }

    public int getQi() {
        return qi;
    }

    public String getMonastery() {
        return monastery;
    }
}
