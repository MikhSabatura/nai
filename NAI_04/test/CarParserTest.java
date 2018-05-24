import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CarParserTest {

    @Test
    public void parseFromFile() {
        List<Car> carList = CarParser.parseFromFile("./car_bayes/training.txt");
        Assert.assertEquals(1705, carList.size());
    }
}