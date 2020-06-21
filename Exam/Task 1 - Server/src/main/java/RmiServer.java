import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServer {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(8080);
            RmiInterface calculatorInterface = new Calculator();
            registry.rebind("calculator", calculatorInterface);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public interface Function {
        double calculate(double x);
    }

    static class Calculator extends UnicastRemoteObject implements RmiInterface {
        Calculator() throws RemoteException {
            super();
        }

        @Override
        public List<Double> sin(double a, double left, double right, double step) {
            return worker(left, right, step, x -> a * Math.sin(x));
        }

        @Override
        public List<Double> cos(double a, double left, double right, double step) {
            return worker(left, right, step, x -> a + Math.cos(x));
        }

        @Override
        public List<Double> tan(double a, double left, double right, double step) {
            return worker(left, right, step, x -> Math.tan(x) - a);
        }

        @Override
        public List<Double> standard(double a, double b, double left, double right, double step) {
            return worker(left, right, step, x -> a * x + b);
        }

        private List<Double> worker(double left, double right, double step, Function function) {
            List<Double> result = new ArrayList<>();
            double x = left;
            while (x <= right) {
                result.add(function.calculate(x));
                x += step;
            }
            return result;
        }
    }
}

