
public class Flower {

    private final String type;
    private final double[] parameters;

    public Flower(double[] parameters, String type) {
        this.parameters = parameters;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public double[] getParameters() {
        return parameters;
    }

}
