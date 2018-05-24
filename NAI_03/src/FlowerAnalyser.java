import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlowerAnalyser {

    final DecimalFormat df = new DecimalFormat("#0.000");

    private List<Flower> flowers;
    private List<Cluster> clusters;

    public FlowerAnalyser(String flowerDataPath) {
        this.flowers = FlowerParser.parseFromFile(flowerDataPath);
    }

    public static void main(String[] args) {
        String filePath = args[0];
        int k = Integer.parseInt(args[1]);

        FlowerAnalyser analyser = new FlowerAnalyser(filePath);
        analyser.kmeans(k);
        analyser.calculatePurity();
    }

    private void kmeans(int k) {
        double[] avg = new double[4];
        for (Flower f :
                flowers) {
            for (int i = 0; i < f.getCoordinates().length; i++) {
                avg[i] += f.getCoordinates()[i];
            }
        }
        for (int i = 0; i < avg.length; i++) {
            avg[i] /= flowers.size();
        }

        clusters = Stream.generate(() -> new Cluster(avg))
                .limit(k)
                .collect(Collectors.toList());

        int count = 0;
        while (true) {
            List<Boolean> resultsUpdated = flowers.stream()
                    .map(f -> updateAssignedCluster(f, clusters))
                    .collect(Collectors.toList());
            System.out.println("iteration " + ++count);
            clusters.forEach(this::printSquareDistances);
            if (!resultsUpdated.contains(true)) {
                break;
            }
            clusters.forEach(c -> updateCentroid(c, flowers));
        }
    }

    private void printSquareDistances(Cluster c) {
        List<Flower> assignedFlowers = flowers.stream()
                .filter(f -> f.getCluster().equals(c))
                .collect(Collectors.toList());
        double sum = assignedFlowers.stream()
                .mapToDouble(f -> calculateSquareDistance(c.getCentroid(), f.getCoordinates()))
                .sum();
        System.out.println("cluster " + c.getId() + " sum = " + sum);
    }

    //returns true when updated
    private boolean updateAssignedCluster(Flower f, List<Cluster> clusters) {
        boolean isUpdated = false;
        for (Cluster c : clusters) {
            if (f.getCluster() == null ||
                    calculateDistance(f.getCoordinates(), f.getCluster().getCentroid()) > calculateDistance(f.getCoordinates(), c.getCentroid())) {
                f.setCluster(c);
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    private void updateCentroid(Cluster c, List<Flower> flowers) {
        List<Flower> assignedFlowers = flowers.stream()
                .filter(f -> f.getCluster().equals(c))
                .collect(Collectors.toList());

        if (assignedFlowers.size() == 0) {
            return;
        }
        double[] newCentroid = new double[4];
        for (Flower f : assignedFlowers) {
            for (int i = 0; i < newCentroid.length; i++) {
                newCentroid[i] += f.getCoordinates()[i];
            }
        }
        for (int i = 0; i < newCentroid.length; i++) {
            newCentroid[i] /= assignedFlowers.size();
        }
        c.setCentroid(newCentroid);
    }

    private double calculateDistance(double[] a, double[] b) {
        double distance = 0;
        for (int i = 0; i < a.length; i++) {
            distance += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(distance);
    }

    private double calculateSquareDistance(double[] a, double[] b) {
        double distance = 0;
        for (int i = 0; i < a.length; i++) {
            distance += Math.pow(a[i] - b[i], 2);
        }
        return distance;
    }

    private void calculatePurity() {
        List<Long> mostOccurringList = new ArrayList<>();

        for (Cluster c : clusters) {
            List<Flower> assignedFlowers = flowers.stream()
                    .filter(f -> f.getCluster().equals(c))
                    .collect(Collectors.toList());

            Map<String, Long> occurrences = assignedFlowers.stream()
                    .collect(Collectors.groupingBy(Flower::getType, Collectors.counting()));

            List<String> sortedOccurrenceKeys = new ArrayList<>(occurrences.keySet());
            sortedOccurrenceKeys.sort((a, b) -> {
                if (occurrences.get(a) > occurrences.get(b)) {
                    return -1;
                } else if (occurrences.get(a) < occurrences.get(b)) {
                    return 1;
                } else {
                    return 0;
                }
            });

            System.out.print("cluster" + c.getId() + ":");
            for (int i = 0; i < sortedOccurrenceKeys.size(); i++) {
                String type = sortedOccurrenceKeys.get(i);
                long occurCount = occurrences.get(type);
                if (i == 0) {
                    mostOccurringList.add(occurCount);
                }
                System.out.print(" " + type + ":" + occurCount);
            }
            System.out.println();

        }
        System.out.print("Purity = (");
        for (int i = 0; i < mostOccurringList.size(); i++) {
            System.out.print(mostOccurringList.get(i));
            System.out.print(+i == mostOccurringList.size() - 1 ? "" : " + ");
        }
        double sum = mostOccurringList.parallelStream().reduce(0L, (a, b) -> a + b);
        System.out.print(") / " + flowers.size() + " = " + df.format(sum / flowers.size()) + "\n");
//        clusters.forEach(System.out::println);
    }

}
