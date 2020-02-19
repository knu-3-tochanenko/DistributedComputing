public class Barrier {
    private int elements;
    private int completedElements = 0;
    private Runnable runnable;

    Barrier(int elements, Runnable runnable) {
        this.elements = elements;
        this.runnable = runnable;
    }

    synchronized void await() {
        completedElements++;
        System.out.println(ANSI.Red("Barrier value is now "
                + ANSI.Yellow(Integer.toString(completedElements))
                + ANSI.Red(" / ")
                + ANSI.Yellow(Integer.toString(elements))));
        if (completedElements < elements) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            completedElements = 0;
            notifyAll();
            runnable.run();
        }
    }
}
