import java.util.Arrays;

public class Car {

    private String[] attributes;

    public Car(String[] attributes) {
        this.attributes = attributes;
    }

    public String getDecision() {
        return attributes[attributes.length - 1];
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return Arrays.toString(attributes);
    }
}
