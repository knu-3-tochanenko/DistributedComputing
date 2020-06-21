import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiInterface extends Remote {
    List<Double> sin(double a, double left, double right, double step) throws RemoteException;

    List<Double> cos(double a, double left, double right, double step) throws RemoteException;

    List<Double> tan(double a, double left, double right, double step) throws RemoteException;

    List<Double> standard(double a, double b, double left, double right, double step) throws RemoteException;
}
