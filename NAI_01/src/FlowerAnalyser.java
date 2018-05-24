import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlowerAnalyser {

    private List<Flower> trainList;
    private List<Flower> testList;

    private FlowerPerceptron perceptron;

    public FlowerAnalyser(String trainPath, String testPath) {
        this.trainList = FlowerParser.parseFromFile(trainPath);
        this.testList = FlowerParser.parseFromFile(testPath);
        this.perceptron = new FlowerPerceptron(trainList.get(0).getType());
    }

    public static void main(String[] args) {
        FlowerAnalyser analyser = new FlowerAnalyser(args[0], args[1]);
        double alpha = Double.parseDouble(args[2]);
        if (alpha <= 0) {
            System.err.println("ILLEGAL ALPHA, CAN ONLY BE A NATURAL NUMBER");
            System.exit(1);
        }
        int iterationCount = Integer.parseInt(args[3]);
        if (iterationCount < 0) {
            System.err.println("ILLEGAL NUMBER OF ITERATIONS, CAN ONLY BE A NON-NEGATIVE NUMBER");
            System.exit(1);
        }
        analyser.trainPerceptron(alpha, iterationCount);
        double correctlyComputed = analyser.testPerceptron();
        double accuracy = correctlyComputed / analyser.testList.size() * 100;
        System.out.printf("Accuracy=%1.2f%%, correctly computed %2.0f out of %1d %n", accuracy, correctlyComputed, analyser.testList.size());
        processUserInput(analyser);
    }

    private static void processUserInput(FlowerAnalyser analyser) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.println("Enter data or press Enter to exit");
            input = scanner.nextLine();
            if (input.isEmpty()) {
                break;
            }
            Flower f = FlowerParser.parseFromString(input);
            if (f == null) {
                System.out.println("Illegal data entered");
                continue;
            }
            boolean correct = analyser.checkOutputCorrectness(f);
            if (correct) {
                System.out.println("The flower's type was correctly inferred");
            } else {
                System.out.println("Wasn't able to infer flower's type");
            }
        }
    }

    private double testPerceptron() {
        System.out.println("Testing perceptron...");
        double correctCount = 0;
        for (Flower f : testList) {
            if (checkOutputCorrectness(f)) {
                correctCount++;
            }
        }
        return correctCount;
    }

    private void trainPerceptron(double alpha, int iterationCount) {
        System.out.println("Training perceptron...");
        for (int i = 0; i < iterationCount; i++) {
            trainList.forEach(f -> assertOutput(f, alpha));
        }
        System.out.println("Training finished");
    }

    private void assertOutput(Flower f, double alpha) {
        int desiredOutput;
        if (f.getType().equals(perceptron.getType())) {
            desiredOutput = FlowerPerceptron.ACTIVATED;
        } else {
            desiredOutput = FlowerPerceptron.NOT_ACTIVATED;
        }
        int computedOutput = perceptron.computeOutput(f);
        if (computedOutput != desiredOutput) {
            perceptron.updateWeights(f, desiredOutput, computedOutput, alpha);
        }
    }

    private boolean checkOutputCorrectness(Flower f) {
        if (f.getType().equals(perceptron.getType())) {
            return perceptron.computeOutput(f) == FlowerPerceptron.ACTIVATED;
        } else {
            return perceptron.computeOutput(f) == FlowerPerceptron.NOT_ACTIVATED;
        }
    }

}
