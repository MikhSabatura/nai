import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileParser {

    public static long parseCapacity(String path) {
        try(BufferedReader bufReader = new BufferedReader(new FileReader(new File(path)))) {
            return Long.parseLong(bufReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("UNABLE TO PARSE THE DATA");
        }
    }

    public static List<Item> parseItems(String path) {
        List<Item> result = new ArrayList<>();
        try(BufferedReader bufReader = new BufferedReader(new FileReader(new File(path)))) {
            bufReader.readLine();
            for(String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
                result.add(parseItem(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("UNABLE TO PARSE THE DATA");
        }
        return result;
    }

    private static Item parseItem(String string) {
        Scanner scanner = new Scanner(string);
        double value = scanner.nextDouble();
        double weight = scanner.nextDouble();
        scanner.close();
        return new Item(value, weight);
    }
}
