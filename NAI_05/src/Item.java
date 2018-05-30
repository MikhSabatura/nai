public class Item {

    private double weight;
    private double value;

    public Item(double value, double weight) {
        this.value = value;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value) + " " + String.valueOf(weight) + "\n";
    }
}
