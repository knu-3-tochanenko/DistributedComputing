public class Main {
    public static void main(String[] args) {
        Calculator c = new Calculator();

        c.addTask(100);
        c.addTask(500);
        c.addTask(1000);
        c.addTask(1500);
        c.addTask(2000);
        c.addTask(2500);
        c.addTask(3000);

        c.run();
    }
}
