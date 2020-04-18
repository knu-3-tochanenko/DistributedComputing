package logger;

import settings.Properties;

import java.io.File;

public class FilesCleaner {
    public static void main(String[] args) {
        File file = new File(Properties.RESULT_PATH);
        File html = new File(Properties.HTML_PATH);

        if (file.delete()) {
            System.out.println("Deleted result.txt");
        }

        if (html.delete()) {
            System.out.println("Deleted table.html");
        }
    }
}
