package Producer;

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

        AggregateClient client = new AggregateClient(HOST, PORT, USER, PASSSWORD, QUEUE);
        PersistenceStrategy persistenceStrategy = new FilePersistenceStrategy(filepath, persistence_file);
        client.setPersistenceStrategy(persistenceStrategy);
        assertFalse(client.isReadyToSend());
        persistenceStrategy.StoreMessage(new SimpleMessage(99,"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n" +
                "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   \n" +
                "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   \n" +
                "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.   \n" +
                "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.   \n" +
                "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur"));
        assertTrue(client.isReadyToSend());






    }


    @Test
    public void RecoveryTest() throws IOException {
       //TODO does not work yet

        AggregateClient client = new AggregateClient(HOST, PORT, USER, PASSSWORD, QUEUE);
        PersistenceStrategy persistenceStrategy = new FilePersistenceStrategy(filepath, persistence_file);
        client.setPersistenceStrategy(persistenceStrategy);

        Message message = new SimpleMessage(99, "Hello World");
        persistenceStrategy.StoreMessage(message);
        client.recoverLastState();
        assertEquals(message.getSequence_number(),client.getSequence_number());




    }


}










