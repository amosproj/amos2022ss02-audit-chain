package ProducerDummy.DataGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/***
 * This is one Implementation which reads Data from a File to construct a Messages.Message.
 */
public class FileDataReader implements DataGenerator {

    /** path of the file were the data are stored */

    private static final String path = Paths.get("src", "main", "java","ProducerDummy").toString();
    private static final String file_name = "household_power_consumption.txt";
    public BufferedReader br;

    /**
     * Prepare the object file to be read and set a (buffered) reader to it.
     *
     * @throws FileNotFoundException if no file with the path specified is found
     * */
    public FileDataReader(String path,String filename) throws FileNotFoundException {

        String file_path = Paths.get(path,file_name).toString();
        File file = new File(file_path);
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
