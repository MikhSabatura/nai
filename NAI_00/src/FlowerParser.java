import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FlowerParser {

    private final static String LEGAL_FLOWER_PATTERN = "(\\d+(\\.\\d)?,){4}\\S+";

    public static List<Flower> parseFromFile(String path) {
        List<Flower> resultList = new ArrayList<>();
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

    public static Flower parseFromString(String str) {
        if (!validateString(str)) {
            return null;
        }
        String[] parsedStrings = str.split(",");
        String[] coordinatesStr = Arrays.copyOf(parsedStrings, parsedStrings.length - 1);

        double[] coordinatesDouble = new double[coordinatesStr.length];
        for (int i = 0; i < coordinatesStr.length; i++) {
            coordinatesDouble[i] = Double.parseDouble(coordinatesStr[i]);
        }
        String type = parsedStrings[parsedStrings.length - 1];

        return new Flower(coordinatesDouble, type);
    }

    private static boolean validateString(String str) {
        return Pattern.compile(LEGAL_FLOWER_PATTERN).matcher(str).matches();
    }

}
