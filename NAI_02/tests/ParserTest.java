import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import parsers.LangParser;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ParserTest {

    private static Class<LangParser> parserClass;

    @Test
    public void normalizeTest() throws Exception {
        int iterationCount = 30;
        StringBuilder builder = new StringBuilder(iterationCount * 26);
        for (int i = 0; i < iterationCount; i++) {
            for (char c = 97; c < 123; c++) {
                if (c % 2 == 0) {
                    builder.append(c).append(" ");
                } else {
                    builder.append(Character.toUpperCase(c)).append(" ");
                }
            }
        }
        Method normalize = parserClass.getDeclaredMethod("normalize", String.class);
        normalize.setAccessible(true);


        String normalStr = (String) normalize.invoke(null, builder.toString());

        Assert.assertEquals(iterationCount * 26, normalStr.length());
        normalStr.chars().forEach(c -> Assert.assertFalse(Character.isUpperCase(c)));
        Assert.assertFalse(normalStr.contains("\\s"));
    }

    @Test
    public void parseFileTest() throws Exception {
        try (Stream<Path> paths = Files.walk(Paths.get("./training"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(this::printFileCharFreq);
        }
    }

    private void printFileCharFreq(File file) {
        System.out.println("\n\n" + file.getName());
        printCharArr(LangParser.calcCharFrequencies(file));

    }

    private void printCharArr(double[] arr) {
        int maxInd = 0;
        int minInd = 0;

        for (int i = 0; i < arr.length; i++) {
            System.out.println(((char) (i + 97)) + "     " + arr[i]);

            if (arr[maxInd] < arr[i]) {
                maxInd = i;
            }
            if(arr[minInd] > arr[i]) {
                minInd = i;
            }
        }
        System.out.println("Max:\n" + ((char) (maxInd + 97)) + "     " + arr[maxInd]);
        System.out.println("Min:\n" + ((char) (minInd + 97)) + "     " + arr[minInd]);
    }

    @Test
    public void normalizeNumberString() throws Exception {
        int capacity = 100;
        StringBuilder builder = new StringBuilder(capacity);
        for (int i = 0; i < capacity; i++) {
            builder.append(i);
            builder.append(" ,-'+=");
        }

        Method normalize = parserClass.getDeclaredMethod("normalize", String.class);
        normalize.setAccessible(true);
        String normal = (String) normalize.invoke(null, builder.toString());
        Assert.assertEquals(0, normal.length());
        System.out.println(normalize.invoke(null , "Григо́рий Я́ковлевич Перельма́н"));
        System.out.println(normalize.invoke(null, "я долбоеб"));
    }

    @BeforeClass
    public static void init() {
        parserClass = LangParser.class;
    }

}
