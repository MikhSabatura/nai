import java.io.IOException;
import java.util.Scanner;

public class UserInterface {

    public final static String OUT_FORMAT = "%-27s %-27s %-5s\n";
    private FlowerAnalyzer flowerAnalyzer;

    public UserInterface(FlowerAnalyzer flowerAnalyzer) {
        this.flowerAnalyzer = flowerAnalyzer;
    }

    public static void main(String[] args) {
        String trainSetPath = args[0];
        String testSetPath = args[1];
        int k = Integer.parseInt(args[2]);
        if (k <= 0) {
            System.out.println("ILLEGAL K, CAN ONLY BE A NATURAL NUMBER");
            System.exit(1);
        }
        //creating an object and loading data from the files
        FlowerAnalyzer flowerAnalyzer = null;
        try {
            flowerAnalyzer = new FlowerAnalyzer(trainSetPath, testSetPath, k);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        UserInterface ui = new UserInterface(flowerAnalyzer);
        ui.performUserInteraction();
    }

    private void performUserInteraction() {
        analiseTestSet();
        processUserInput();
    }

    private void analiseTestSet() {
        double accurateResultCount = 0;
        System.out.printf(OUT_FORMAT, "Predicted", "Test", "Match");
        for (Flower f : flowerAnalyzer.getTestSet()) {
            if(analiseFlowerType(f)) {
                accurateResultCount++;
            }
        }
        double accuracyPercent = accurateResultCount / flowerAnalyzer.getTestSet().size() * 100;
        System.out.printf("\nAccuracy: %.2f%%\n", accuracyPercent);
    }

    private boolean analiseFlowerType(Flower f) {
        String predictedType = flowerAnalyzer.knn(f);
        String realType = f.getType();
        boolean equal = realType.equals(predictedType);
        System.out.printf(OUT_FORMAT, predictedType, realType, equal);
        return equal;
    }

    private void processUserInput() {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.println("Enter data or press Enter to exit");
            input = scanner.nextLine();
            if(input.isEmpty()) {
                break;
            }
            Flower f = FlowerParser.parseFromString(input);
            if (f == null) {
                System.out.println("Illegal data entered");
                continue;
            }
            System.out.printf(OUT_FORMAT, "Predicted", "Test", "Match");
            analiseFlowerType(f);
            System.out.println();
        }
    }

}
