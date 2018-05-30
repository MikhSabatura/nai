public class Solution {

    private final int vector;
    private final double valueSum;
    private final double weightSum;

    public Solution(int vector, double valueSum, double weightSum) {
        this.vector = vector;
        this.valueSum = valueSum;
        this.weightSum = weightSum;
    }

    @Override
    public String toString() {
        return "vector: " + Integer.toBinaryString(vector) + "\n" +
                "values sum: " + valueSum + "\n" +
                "weights sum: " + weightSum;
    }

    public int getVector() {
        return vector;
    }

    public double getValueSum() {
        return valueSum;
    }

    public double getWeightSum() {
        return weightSum;
    }
}
