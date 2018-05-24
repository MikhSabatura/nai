package analysers;

import parsers.LangParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LangAnalyser {

    private List<Perceptron> perceptrons;

    private File trainDir;
    private Map<File, double[]> trainFilesData;

    private static final int ACTIVATED = 1;
    private static final int NOT_ACTIVATED = 0;

    public LangAnalyser(String path) {
        this.trainDir = new File(path);
        if (!trainDir.isDirectory()) {
            System.err.println(path + " IS NOT A DIRECTORY");
            System.exit(1);
        }
        this.perceptrons = initializePerceptrons(trainDir);
        try {
            this.trainFilesData = processFileData(trainDir);
        } catch (IOException e) {
            System.out.println("ERROR WHILE PROCESSING FILES");
            System.exit(1);
        }
    }

    private List<Perceptron> initializePerceptrons(File dir) {
        List<Perceptron> result = new ArrayList<>();
        for (File langDir : dir.listFiles()) {
            if (!langDir.isDirectory()) {
                System.err.println(langDir.getName() + " IS NOT A DIRECTORY");
                System.exit(1);
            }
            result.add(new Perceptron(langDir.getName()));
        }
        return result;
    }

    private Map<File, double[]> processFileData(File trainDir) throws IOException {
        Map<File, double[]> result = new HashMap<>();
        Files.walk(trainDir.toPath())
                .map(Path::toFile)
                .filter(File::isFile)
                .forEach(f -> result.put(f, LangParser.calcCharFrequencies(f)));
        return result;
    }

    public void trainPerceptrons(double maxError, double learnCoef) throws IOException {
        processFileData(trainDir);
        long count = 0;
        while (true) {
            double error = Files.walk(trainDir.toPath())
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .flatMapToDouble(file -> perceptrons.stream()
                            .mapToDouble(perceptron -> assertOutput(file, perceptron, learnCoef)))
                    .reduce(0, Double::sum);
            if (error < maxError) {
                break;
            }
            if(++count % 4000 == 0) {
                System.out.println(error - maxError);
            }
        }
    }

    public String analyseLanguage(File file) {
        double[] frequencies = LangParser.calcCharFrequencies(file);
        return findMostActivatedPerceptron(frequencies).getType();
    }

    public String analyseLanguage(String string) {
        double[] frequencies = LangParser.calcCharFrequencies(string);
        return findMostActivatedPerceptron(frequencies).getType();
    }

    private Perceptron findMostActivatedPerceptron(double[] inputs) {
        Map<Perceptron, Double> resultMap = new HashMap<>();
        Comparator<Map.Entry<Perceptron, Double>> entryComparator = (a, b) -> {
            if (a.getValue() == b.getValue()) {
                return 0;
            } else if (a.getValue() > b.getValue()) {
                return 1;
            }
            return -1;
        };
        perceptrons.forEach(p -> resultMap.put(p, p.computeOutput(inputs)));
        return resultMap.entrySet().stream()
                .max(entryComparator).get().getKey();
    }

    private double assertOutput(File file, Perceptron perceptron, double learnCoef) {
        String langDirName = file.getAbsoluteFile().getParentFile().getName();
        double desiredOutput = perceptron.getType().equals(langDirName) ? ACTIVATED : NOT_ACTIVATED;

        // all the data is stored in a map in order to avoid calculating the frequency each iteration
        double[] inputs = trainFilesData.get(file);

        //computing output and updating weights
        double actualOutput = perceptron.computeOutput(inputs);
        if (desiredOutput != actualOutput) {
            perceptron.updateWeights(inputs, desiredOutput, actualOutput, learnCoef);
        }

        //returning the error for further accumulation
        return calcError(desiredOutput, actualOutput);
    }

    private double calcError(double desired, double acutal) {
        return 0.5 * Math.pow(desired - acutal, 2);
    }

}
