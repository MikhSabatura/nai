import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TSP_Parser {

    public static List<List<Integer>> parseDistances(String path) throws IOException {
        List<List<Integer>> result = new ArrayList<>();
        BufferedReader bufReader = new BufferedReader(new FileReader(path));
        for (String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
            line = line.trim();
            String[] distStrings = line.split("\\s+");
            Integer[] distArr = new Integer[distStrings.length];
            for (int i = 0; i < distStrings.length; i++) {
                distArr[i] = Integer.parseInt(distStrings[i]);
            }
            result.add(Arrays.asList(distArr));
        }
        return result;
    }
}
