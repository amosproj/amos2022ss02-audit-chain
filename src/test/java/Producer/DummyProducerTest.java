package Producer;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.*;

public class DummyProducerTest {
    String HOST;
    int PORT;
    String USER;
    String PASSSWORD;
    String QUEUE;
    String filepath;
    String persistence_file = "textmessage.txt";

    @Before
    public void setUp() throws IOException {
        filepath = Paths.get(System.getProperty("user.dir"),"src", "main", "resources","ProducerDummy").toString();
        String filename = "config.properties";

        Path config_path = Paths.get(filepath, filename);
        Properties p = new Properties();
        FileReader reader = new FileReader(config_path.toString());
        p.load(reader);

        HOST = p.getProperty("HOST");
        PORT = Integer.parseInt(p.getProperty("PORT"));
        USER = p.getProperty("USERNAME");
        PASSSWORD = p.getProperty("PASSWORD");
        QUEUE = "FAKE";
    }


    @Before @After
    public void DeleteFile() {
        File file = new File(Paths.get(filepath, persistence_file).toString());
        file.delete();
    }


    @Test
    public void isReadyToSendTest() throws IOException {






    }


    @Test
    public void RecoveryTest() throws IOException {




    }


}










