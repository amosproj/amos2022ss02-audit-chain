package BlockchainImplementation.DataGeneratorBlockchain.DataGeneration;

import java.io.IOException;

/***
 * Data Generator interface.
 */
public interface DataGenerator {

    /**
     * Read the next line from the file and return it as a String.
     *
     * @return the line read
     * */
    public String getData();

    /**
     * Read the line after a number of lines skipped and return it as a String.
     * Used in recovery mode, to recover the last line read.
     *
     * @param sequence_number number of lines to be skipped
     *
     * @return the line read
     * */
    public String getData(int sequence_number);

}
