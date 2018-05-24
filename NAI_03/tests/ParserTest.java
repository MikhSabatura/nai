import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void test_parseFromFile() throws Exception {
        Assert.assertEquals(100, FlowerParser.parseFromFile("./iris_data/iris_data.txt").size());
    }

}
