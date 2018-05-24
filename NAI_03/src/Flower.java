public class Flower {

    private final String type;
    private final double[] parameters;
    private Cluster cluster;

    public Flower(double[] parameters, String type) {
        this.parameters = parameters;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return parameters;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public String toString() {
        return type;
    }
}
