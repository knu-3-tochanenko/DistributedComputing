package logger;

import settings.Properties;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TableCreator {
    private static final String TABLE = "<table  border=\"1\">\n \t <tr>\n \t\t <th>%s</th>\n \t\t <th>1 Core</th>\n \t\t <th colspan=\"2\">4 Cores</th>\n \t </tr>\n \t <tr>\n \t\n <td>Size</td>\n \t\n <td>Time</td>\n \t\n <td>Time</td>\n \t\n <td>Acceleration</td>\n \t </tr>\n \t <tr>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t </tr>\n \t <tr>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t </tr>\n \t <tr>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t\n <td>%s</td>\n \t </tr>\n </table><br>\n";

    private static String[] generateReport(String name, List<String> results, int offset) {
        String[] res = new String[13];
        res[0] = name;

        res[1] = results.get(2);
        res[2] = results.get(3);
        res[3] = results.get(15);

        for (int i = 0; i < 3; i++) {
            res[i * 4 + 1] = results.get(i * 4 + 2 + offset);
            res[i * 4 + 2] = results.get(i * 4 + 3 + offset);
            res[i * 4 + 3] = results.get(i * 4 + 15 + offset);
            res[i * 4 + 4] =
                    String.valueOf(Long.parseLong(results.get(i * 4 + 3 + offset)) * 1.0
                            / Long.parseLong(results.get(i * 4 + 15 + offset)));
        }

        return res;
    }

    public static void main(String[] args) {
        File file = new File(Properties.RESULT_PATH);
        File html = new File(Properties.HTML_PATH);

        FileReader fr = null;
        BufferedReader br = null;

        FileWriter fw = null;

        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            fw = new FileWriter(html, false);

            String line = "";

            List<String> arr = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                arr.add(line);
            }

            Comparator<String> comparator = (String a, String b) -> {
                String[] lA = a.split(" ");
                String[] lB = b.split(" ");

                if (lA[0].compareTo(lB[0]) == 0) {
                    if (Integer.valueOf(lA[1]).equals(Integer.valueOf(lB[1])))
                        return Integer.parseInt(lA[2]) - Integer.parseInt(lB[2]);
                    else
                        return Integer.parseInt(lA[1]) - Integer.parseInt(lB[1]);
                } else
                    return lA[0].compareTo(lB[0]);

            };

            arr.sort(comparator);

            arr.forEach(System.out::println);

            List<String> splitted = new ArrayList<>();
            for (String str : arr) {
                String[] splittedBetweenWhiteSpaces = str.split(" ");
                splitted.addAll(Arrays.asList(splittedBetweenWhiteSpaces));
            }

            fw.write(String.format(TABLE,generateReport("Sequential", splitted, 0)));
            fw.write(String.format(TABLE,generateReport("Cannon", splitted, 24)));
            fw.write(String.format(TABLE,generateReport("Fox", splitted, 48)));
            fw.write(String.format(TABLE,generateReport("String", splitted, 72)));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fr != null;
            assert fw != null;
            try {
                fr.close();
                fw.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
