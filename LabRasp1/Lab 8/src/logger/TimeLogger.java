package logger;

import settings.Properties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TimeLogger {
    public static void log(String name, int size, int cores, long score) {
        switch (name) {
            case "B":
                System.out.print("Simple");
                break;
            case "S":
                System.out.print("String");
                break;
            case "F":
                System.out.print("Fox   ");
                break;
            case "C":
                System.out.print("Cannon");
                break;
        }

        File file = new File(Properties.RESULT_PATH);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            fw.write(name + " " + cores + " " + size + " " + score + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fw != null;
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(" of size " + size + " on " + cores + " cores: " + score + " ms.");
    }
}
