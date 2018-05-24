import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NaiveBayesAnalyzer {

    private List<Car> trainList;
    private List<List<String>> attributesOptions;

    public NaiveBayesAnalyzer(List<Car> trainList) {
        this.trainList = trainList;
        this.attributesOptions = getAttributesOptions();
    }

    public static void main(String[] args) {
        String trainPath = args[0];
        String testPath = args[1];
        List<Car> trainList = CarParser.parseFromFile(trainPath);
        List<Car> testList = CarParser.parseFromFile(testPath);

        NaiveBayesAnalyzer analyzer = new NaiveBayesAnalyzer(trainList);
        analyzer.analyseTestList(testList);
        processUserInput(analyzer);
    }

    private static void processUserInput(NaiveBayesAnalyzer analyser) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.println("Enter data or press Enter to exit");
            input = scanner.nextLine();
            if (input.isEmpty()) {
                break;
            }
            Car f = CarParser.parseFromString(input);
            if (f == null) {
                System.out.println("Illegal data entered");
                continue;
            }
            analyser.analyseTestCar(f);
        }
    }

    private void analyseTestList(List<Car> testList) {
        double correctCount = 0;
        for (Car car : testList) {
            if (analyseTestCar(car)) {
                correctCount++;
            }
        }
        System.out.printf("Correctly predicted %4.0f out of %4d, precision: %3.3f\n",
                correctCount, testList.size(), correctCount / testList.size());
    }

    private boolean analyseTestCar(Car car) {
        String predicted = predictCarType(car);
        boolean isCorrect = predicted.equals(car.getDecision());
        System.out.printf("predicted: %7s | actual: %7s | %1s\n", predicted, car.getDecision(), isCorrect ? "+" : "-");
        return isCorrect;
    }

    private String predictCarType(Car car) {
        Map<String, Double> decisionProbabilities = attributesOptions.get(attributesOptions.size() - 1).stream()
                .collect(Collectors.toMap(s -> s, s -> computeDecisionProbability(s, car)));
        String maxKey = null;
        for (Map.Entry<String, Double> entry : decisionProbabilities.entrySet()) {
            if (maxKey == null || decisionProbabilities.get(maxKey) < entry.getValue()) {
                maxKey = entry.getKey();
            }
        }
        return maxKey;
    }

    private double computeDecisionProbability(String decision, Car car) {
        double result = 1;
        List<Car> filteredByDecision = trainList.stream()
                .filter(c -> c.getDecision().equals(decision))
                .collect(Collectors.toList());
        for (int i = 0; i < car.getAttributes().length; i++) {
            if (i == car.getAttributes().length - 1) {
                result *= computeAttribProbability(i, decision, trainList);
            } else {
                result *= computeAttribProbability(i, car.getAttributes()[i], filteredByDecision);
            }
        }
        return result;
    }

    public double computeAttribProbability(int indx, String val, List<Car> universe) {
        double nominator = universe.stream()
                .filter(c -> c.getAttributes()[indx].equals(val))
                .count() + 1;
        double denominator = universe.size() + attributesOptions.get(indx).size();
        return nominator / denominator;
    }

    private List<List<String>> getAttributesOptions() {
        Car car = trainList.get(0);
        List<List<String>> result = new ArrayList<>();

        for (int i = 0; i < car.getAttributes().length; i++) {
            int indx = car.getAttributes().length - (car.getAttributes().length - i);
            List<String> currAttribOptions = trainList.stream()
                    .map(c -> c.getAttributes()[indx])
                    .distinct()
                    .collect(Collectors.toList());
            result.add(currAttribOptions);
        }
        return result;
    }


}
