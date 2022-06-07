package Test;

import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.FileDataReader;
import org.junit.Test;
import org.junit.Assert;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class DataGeneratorTest {

    @Test
    public void ReadFileTest() throws FileNotFoundException {
        String first_line = "Date;Time;Global_active_power;Global_reactive_power;Voltage;Global_intensity;Sub_metering_1;Sub_metering_2;Sub_metering_3";
        DataGenerator dataGenerator = new FileDataReader();
        assertEquals(first_line,dataGenerator.getData());
    }

    @Test
    public void ReadLineXFileTest() throws FileNotFoundException {
        String sixth_line = "16/12/2006;17:28:00;3.666;0.528;235.680;15.800;0.000;1.000;17.000";
        DataGenerator dataGenerator = new FileDataReader();
        assertEquals(sixth_line,dataGenerator.getData(6));

    }





}
