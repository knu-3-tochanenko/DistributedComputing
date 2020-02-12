public class Main {
    public static void main(String[] args) {
        HoneyJar jar = new HoneyJar(Settings.JAR_VOLUME);
        MySemaphore semaphore = new MySemaphore();
        Bear bear = new Bear(jar, semaphore);
        Bee[] bees = new Bee[Settings.BEES_COUNT];

        new Thread(bear).start();

        for (int i = 0; i < Settings.BEES_COUNT; i++) {
            bees[i] = new Bee(jar, bear, semaphore, i);
            new Thread(bees[i]).start();
        }
    }
}
