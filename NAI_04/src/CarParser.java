import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CarParser {

    private static final String LEGAL_CAR_REGEX = "^(\\w+,){6}\\w+$";

    public static List<Car> parseFromFile(String path) {
        List<Car> resultList = new ArrayList<>();
        File file = new File(path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                resultList.add(parseFromString(line));
            }
        } catch (IOException e) {
            System.out.println("FILE NOT FOUND: " + path);
            System.exit(1);
        }
        return resultList;
    }

    public static Car parseFromString(String str) {
        return validateString(str) ? new Car(str.split(",")) : null;
    }

    private static boolean validateString(String string) {
        return Pattern.compile(LEGAL_CAR_REGEX).matcher(string).matches();
    }

}
