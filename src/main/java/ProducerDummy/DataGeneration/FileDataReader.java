package ProducerDummy.DataGeneration;

import java.io.*;


/***
 * This is one Implementation which reads Data from a File to construct a Messages.Message.
 */
public class FileDataReader implements DataGenerator {

    /** path of the file were the data are stored */
    public static final String path = "\\ProducerDummy\\src\\main\\household_power_consumption.txt";
    public BufferedReader br;

    /**
     * Prepare the object file to be read and set a (buffered) reader to it.
     *
     * @throws FileNotFoundException if no file with the path specified is found
     * */
    public FileDataReader() throws FileNotFoundException {
        File file = new File(System.getProperty("user.dir") + FileDataReader.path);
        FileReader fr = new FileReader(file);
        this.br = new BufferedReader(fr);
    }


    @Override
    // get Data
    public String getData() {
        String line = null;
        try {
            line = this.br.readLine();
        } catch (IOException e) {
            System.out.println("Could Not Read from File" + e);
        }
        return line;
    }


    @Override
    // set pointer to last Data read
    public String getData(int sequence_number) {
        String line = null;
        for (int i = 0; i < sequence_number; i++) {
            try {
                line = this.br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return line;
    }
}
