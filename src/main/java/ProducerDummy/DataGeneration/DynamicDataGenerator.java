package ProducerDummy.DataGeneration;

import java.util.Locale;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

/***
 * This is one Implementation which generates dynamic messages
 */

public class DynamicDataGenerator implements DataGenerator {

    FakeValuesService fakeValuesService = new FakeValuesService(
            new Locale("en-US"), new RandomService());

    @Override
    // get Data
    public String getData() {

        String line = null;

        try {
            line = fakeValuesService.regexify("[a-zA-Z0-9]{100}");
        } catch (Exception e) {
            System.out.println("Message generation failed" + e);
        }

        return line;
    }


    @Override
    public String getData(int sequence_number) {

        String line = null;

        for (int i = 0; i < sequence_number; i++) {

            try {
                line = fakeValuesService.regexify("[a-zA-Z0-9]{100}");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return line;
    }
    
}