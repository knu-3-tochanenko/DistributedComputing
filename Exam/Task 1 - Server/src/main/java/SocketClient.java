import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

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
            startConnection("localhost", 8080);
            sendMessage(
                    function + ";" + a + ";" + b + ";"
                            + left+ ";" + right + ";" + step + ";"
            );
            stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static void sendMessage(String msg) throws IOException {
        out.println(msg);
        StringBuilder res = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}