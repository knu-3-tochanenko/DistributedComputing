import java.util.Random;

public class StringGenerator {

    static char[] chars = {'A', 'B', 'C', 'D'};
    static Random random = new Random(System.currentTimeMillis());

    static StringBuffer generate() {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < S.STRING_LEN; i++) {
            res.append(chars[random.nextInt(4)]);
        }

        return res;
    }
}
