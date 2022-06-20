package Producer;

import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.FileDataReader;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.Test;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataGeneratorTest {

    String filepath = Paths.get("src", "main", "resources","ProducerDummy").toString();
    String base_path = Paths.get(System.getProperty("user.dir"),filepath).toString();
    String filename = "household_power_consumption.txt";


    @Test
    @DisplayName("Read First line of File")
    public void ReadFileTest() throws FileNotFoundException {
        String first_line = "Date;Time;Global_active_power;Global_reactive_power;Voltage;Global_intensity;Sub_metering_1;Sub_metering_2;Sub_metering_3";
        DataGenerator dataGenerator = new FileDataReader(base_path,filename);
        assertEquals(first_line,dataGenerator.getData());
    }

    @Test
    @DisplayName("Read the X Line")
    public void ReadLineXFileTest() throws FileNotFoundException {
        String sixth_line = "16/12/2006;17:28:00;3.666;0.528;235.680;15.800;0.000;1.000;17.000";
        DataGenerator dataGenerator = new FileDataReader(base_path,filename);
        assertEquals(sixth_line,dataGenerator.getData(6));

    }

    @Test
    public void checkGeneratedPatternTest() throws Exception {

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        String alphaNumericString = fakeValuesService.regexify("[a-zA-Z0-9]{100}");
        Matcher alphaNumericMatcher = Pattern.compile("[a-zA-Z0-9]{100}").matcher(alphaNumericString);

        assertTrue(alphaNumericMatcher.find());
    }

}
