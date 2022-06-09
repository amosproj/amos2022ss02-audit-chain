package Test;

import ProducerDummy.Client.AbstractClient;
import ProducerDummy.Client.AggregateClient;
import ProducerDummy.Client.Client;
import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.FileDataReader;
import ProducerDummy.Messages.AggregateMessage;
import ProducerDummy.Messages.Message;
import ProducerDummy.Messages.SimpleMessage;
import ProducerDummy.Persistence.FilePersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class DummyProducerTest {
    String HOST = "src/main/Test";
    int PORT = 123;
    String USER = "test";
    String PASSSWORD = "test";
    String QUEUE = "FAKE";


    @Before @After
    public void DeleteFile() {
        String current_path = System.getProperty("user.dir");
        String path = "";
        String filename = "testfile.txt";

        String s = Paths.get(current_path, path, filename).toString();
        File file = new File(Paths.get(current_path, path, filename).toString());
        file.delete();
    }


    @Test
    public void RecoveryTest() throws IOException {
       //TODO does not work yet

        /*
        String path = "";
        String filename = "testfile.txt";


        AggregateClient client = new AggregateClient(HOST, PORT, USER, PASSSWORD, QUEUE);
        DataGenerator dataGenerator = new FileDataReader();
        PersistenceStrategy persistenceStrategy = new FilePersistenceStrategy(path, filename);
        Message message = new SimpleMessage(99, "Hello World");
        persistenceStrategy.StoreMessage(message);
        client.recoverLastState();
        assertEquals(message.getSequence_number(),client.getSequence_number());
        */



    }


}










