import java.util.Random;

public class Settings {
    private static Random random = new Random(System.currentTimeMillis());
    public static final String NAMES[] = {
            "Ben",
            "Paul",
            "Gustav",
            "Tobias",
            "Lukas",
            "Sofia",
            "Emilia",
            "Mila",
            "Ella",
            "Lea"
    };

    public static final String SURNAMES[] = {
            "Weber",
            "Fisher",
            "Bauer",
            "Schmidt",
            "Klein"
    };

    public static final int SLEEP_TIME = 1000;

    public static final String FILE_NAME = "database.map";
    public static final String TEMP_FILE_NAME = "file.temp";

    public static String getRandomName() {
        return NAMES[random.nextInt(NAMES.length)];
    }

    public static String getRandomSurname() {
        return SURNAMES[random.nextInt(SURNAMES.length)];
    }


    public static long getRandomPhone() {
        return random.nextInt(899_99_99) + 100_00_00;
    }
}
