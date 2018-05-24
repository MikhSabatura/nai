import java.util.Arrays;
import java.util.Random;

public class Cluster {

    private static int count = 0;

    private final int id;
    private double[] centroid;

    public Cluster(double[] avg) {
        this.id = ++count;
        this.centroid = new double[4];
        Random rand = new Random();
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] = avg[i] + rand.nextDouble();
        }
//        System.out.println(this);
    }

    public int getId() {
        return id;
    }

    public double[] getCentroid() {
        return centroid;
    }

    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cluster cluster = (Cluster) o;

        if (id != cluster.id) return false;
        return Arrays.equals(centroid, cluster.centroid);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Arrays.hashCode(centroid);
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(id) + " " + Arrays.toString(centroid);
    }
}
