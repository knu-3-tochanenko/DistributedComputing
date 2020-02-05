public class Item {
    private volatile int price;
    private int code;

    Item(int code, int price) {
        this.code = code;
        this.price = price;
    }

    public synchronized int getPrice() {
        return price;
    }

    public synchronized int getCode() {
        return code;
    }
}
