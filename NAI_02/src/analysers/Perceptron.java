package analysers;

public class Perceptron {

    private String type;
    private double[] weights = new double[26];
    private double theta;

    public Perceptron(String type) {
        this.type = type;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
        }
        this.theta = Math.random();
    }

    public double computeOutput(double[] inputs) {
        //unipolar
        //f(x) = 1 / (1 + e^(-net))
        return 1 / (1 + Math.pow(Math.E, -net(inputs)));
    }

    public void updateWeights(double[] inputs, double desired, double actual, double learnCoef) {
        //wnew =wold +η(d−y)y(1−y)x
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learnCoef * actual * (desired - actual) * (1 - actual) * inputs[i];
        }
//        normalize();
    }

    private void normalize() {
        double magnitude = magnitude();
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= magnitude;
        }
        theta /= magnitude;
    }

    private double magnitude() {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * weights[i];
        }
        sum += theta * theta;
        return Math.sqrt(sum);
    }

    private double net(double[] inputs) {
        return dotProduct(weights, inputs) - theta;
    }

    private double dotProduct(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new RuntimeException("CANNOT COMPUTE DOT PRODUCT");
        }
        double result = 0;
        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
