package parsers;

import java.io.*;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class LangParser {

    public static double[] calcCharFrequencies(File file) {
        return occurrencesToFrequencies(countChars(file));
    }

    public static double[] calcCharFrequencies(String str) {
        return occurrencesToFrequencies(countChars(str));
    }

    private static double[] occurrencesToFrequencies(Map<Character, Long> occurrenceMap) {
        double[] result = new double[26];
        long charCount = occurrenceMap.entrySet().parallelStream()
                .mapToLong(Map.Entry::getValue)
                .reduce(0, (a, b) -> a + b);
        occurrenceMap.forEach((k, v) -> result[k % 97] = (double) v / charCount);
        return result;
    }

    private static Map<Character, Long> countChars(File file) {
        Map<Character, Long> result = new HashMap<>(26);

        try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
            for (String line; (line = bufReader.readLine()) != null; ) {
                countChars(line).forEach((tempK, tempV)
                        -> result.compute(tempK, (resK, resV) -> resV == null ? tempV : resV + tempV));
            }
        } catch (IOException e) {
            System.err.println("FILE NOT FOUND " + file.getAbsolutePath());
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }

    private static Map<Character, Long> countChars(String str) {
        Map<Character, Long> result = new HashMap<>(26);
        normalize(str).chars().forEach(key -> result.compute((char) key, (k, v) -> v == null ? 1 : ++v));
        return result;
    }

    private static String normalize(String str) {
        if (str.matches("[а-яА-ЯёЁ]+.*")) {
            str = Transliterator.transliterate(str);
        }
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replaceAll("[^a-zA-Z]", "");
    }

}
