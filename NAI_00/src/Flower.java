
public class Flower {

    private String type;
    private double[] attributes;

    public Flower(double[] attributes, String type) {
        this.attributes = attributes;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getAttributes() {
        return attributes;
    }

    public void setAttributes(double[] attributes) {
        this.attributes = attributes;
    }
}
