import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

public class SocketServer {

    protected int serverPort = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    private PrintWriter out;
    private BufferedReader in;

    public SocketServer(int port) {
        this.serverPort = port;
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketServer socket = new SocketServer(8080);
        socket.run();
    }

    public  void run() {
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }

            String msg = null;
            try {
                msg = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Received message " + msg);

            String[] input = msg.split(";");

            int formula = Integer.parseInt(input[0]);
            double a = Double.parseDouble(input[1]);
            double b = Double.parseDouble(input[2]);
            double left = Double.parseDouble(input[3]);
            double right = Double.parseDouble(input[4]);
            double step = Double.parseDouble(input[5]);


            new Thread(
                    new Computer(out, formula, a, b, left, right, step)
            ).start();


        }
        System.out.println("Server Stopped.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }
}

class Computer implements Runnable {

    private PrintWriter out;
    private int formula;
    private double a;
    private double b;
    private double left;
    private double right;
    private double step;

    Computer(PrintWriter out, int formula, double a, double b, double left, double right, double step) {
        this.out = out;
        this.formula = formula;
        this.a = a;
        this.b = b;
        this.left = left;
        this.right = right;
        this.step = step;
    }

    @Override
    public void run() {
        DecimalFormat df = new DecimalFormat("#00.0000");
        StringBuilder output = new StringBuilder("|\tx\t|\tf(x)\t|\n");
        double x = left;
        System.out.println("Started computing");
        while (x < right) {
            output.append("| ");
            output.append(df.format(x));
            output.append("\t| ");
            output.append(df.format(compute(x)));
            output.append("\t|\n");

            x += step;
            System.out.println("Computing...");
        }

        System.out.println("Finished computing");

        out.print(output.toString());
        out.flush();
    }

    private double compute(double x) {
        switch (formula) {
            case 1:
                return a * Math.sin(x);
            case 2:
                return a + Math.cos(a);
            case 3:
                return Math.tan(x) + a;
            case 4:
                return a * x + b;
        }
        return 0.0;
    }
}