
public class FlowerPerceptron {

    private final String type;
    private double theta;

    private double[] weights = new double[4];

    public final static int ACTIVATED = 1;
    public final static int NOT_ACTIVATED = 0;

    public FlowerPerceptron(String type) {
        this.type = type;
        this.theta = Math.random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
        }
    }

    public void updateWeights(Flower f, int desiredOutput, int computedOutput, double alpha) {
        int delta = desiredOutput - computedOutput;
        for (int i = 0; i < weights.length; i++) {
            weights[i] += delta * alpha * f.getParameters()[i];
        }
        theta -= delta * alpha;
    }

    public int computeOutput(Flower f) {
        double sum = 0;
        //calculating dot-product of weights and flower parameters:
        for (int i = 0; i < f.getParameters().length; i++) {
            sum += f.getParameters()[i] * weights[i];
        }
        return sum >= theta ? ACTIVATED : NOT_ACTIVATED;
    }

    public String getType() {
        return type;
    }

    public double getTheta() {
        return theta;
    }

    public double[] getWeights() {
        return weights;
    }


}
