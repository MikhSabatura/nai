import java.io.IOException;
import java.util.*;

public class FlowerAnalyzer {

    private Collection<Flower> trainSet;
    private Collection<Flower> testSet;
    private int k;

    public FlowerAnalyzer(String trainSetPath, String testSetPath, int k) throws IOException {
        this.trainSet = FlowerParser.parseFromFile(trainSetPath);
        this.testSet = FlowerParser.parseFromFile(testSetPath);
        this.k = k;
        if(k > trainSet.size()) {
            throw new IOException("K IS LARGER THAN THE TRAIN SET");
        }
    }

    public String knn(Flower f) {
        //sorting all flowers in the collection based on their distance from the given flower
        Comparator<Flower> comparator = (a, b) -> {
            double d = calculateDistance(f, a) - calculateDistance(f, b);
            if (d > 0) {
                return 1;
            } else if (d < 0) {
                return -1;
            } else {
                return 0;
            }
        };
        PriorityQueue<Flower> pq = new PriorityQueue<>(comparator);
        pq.addAll(trainSet);

        //mapping the flower type to their occurrences in the nearest neighbour set
        Map<String, Integer> typeMap = new HashMap<>();
        for (int i = 0; i < k; i++) {
            String type = pq.poll().getType();
            typeMap.put(type, typeMap.getOrDefault(type, 0) + 1);
        }
        return getMostOccurringType(typeMap);
    }

    public double calculateDistance(Flower a, Flower b) {
        double distance = 0;
        for (int i = 0; i < a.getAttributes().length; i++) {
            distance += Math.pow(a.getAttributes()[i] - b.getAttributes()[i], 2);
        }
        return Math.sqrt(distance);
    }

    //returns type with with most occurrences in the map
    private String getMostOccurringType(Map<String, Integer> typeMap) {
        if (typeMap.isEmpty()) {
            return null;
        }
        Map.Entry<String, Integer> maxOccurringType = null;
        for (Map.Entry<String, Integer> entry : typeMap.entrySet()) {
            if (maxOccurringType == null || maxOccurringType.getValue() < entry.getValue()) {
                maxOccurringType = entry;
            }
        }
        return maxOccurringType.getKey();
    }

    public Collection<Flower> getTrainSet() {
        return trainSet;
    }

    public void setTrainSet(Collection<Flower> trainSet) {
        this.trainSet = trainSet;
    }

    public Collection<Flower> getTestSet() {
        return testSet;
    }

    public void setTestSet(Collection<Flower> testSet) {
        this.testSet = testSet;
    }
}
