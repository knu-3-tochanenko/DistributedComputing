import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class RmiClient {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.println("What function do you want?" +
                "1: a*sin(x), 2: a+cos(x), 3: tan(x)-a, 4: ax+b");
        int function = s.nextInt();

        System.out.println("Input a parameter:");
        double a = s.nextDouble();
        double b = 0.0;
        if (function == 4) {
            System.out.println("Input b parameter:");
            b = s.nextDouble();
        }

        System.out.println("Input left border:");
        double left = s.nextDouble();

        System.out.println("Input right border:");
        double right = s.nextDouble();

        System.out.println("Input step:");
        double step = s.nextDouble();

        try {
            RmiInterface st = (RmiInterface) Naming.lookup("rmi://localhost:8080/calculator");

            switch (function) {
                case 1:
                    printTable(st.sin(a, left, right, step), left, step);
                    break;
                case 2:
                    printTable(st.cos(a, left, right, step), left, step);
                    break;
                case 3:
                    printTable(st.tan(a, left, right, step), left, step);
                    break;
                case 4:
                    printTable(st.standard(a, b, left, right, step), left, step);
                    break;
                default:
                    throw new IllegalArgumentException();
            }

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static void printTable(List<Double> results, double xStart, double step) {
        double x = xStart;
        System.out.println(String.format("%30s %25s %10s", "x", "|", "f(x)"));
        System.out.println(String.format("%s", "--------------------------------------------------------------"));

        for (Double result : results) {
            System.out.println(String.format("%30s %25s %10s", x, "|", result));
            x += step;
        }
    }
}
