package Producer;


import ProducerDummy.ChannelSelection.RabbitMQChannel;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.Client.Client;
import ProducerDummy.Client.Producer;
import ProducerDummy.DataGeneration.DynamicDataGenerator;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;


public class DummyProducerTest {
    String HOST;
    int PORT;
    String USER;
    String PASSSWORD;
    String QUEUE;
    String persistence_file = "textmessage.txt";
    String filepath = Paths.get(System.getProperty("user.dir"),persistence_file).toString();

    @Before
    public void setUp() throws IOException {

        HOST = "127.0.0.1";
        USER = "user";
        PASSSWORD = "admin";
        QUEUE = "FAKE";
    }


    @Before @After
    public void DeleteFile() {
        File file = new File(Paths.get(filepath, persistence_file).toString());
        if((file.exists())){
            file.delete();
        }
    }


@Test
    public void sendMessage() throws IOException, InterruptedException {
        String queue_name = "TEST";
        RabbitMQChannel channel = new StandardQueue(queue_name);
        Producer producer = new Client(HOST,PORT,USER,PASSSWORD);
        producer.factory = new MockConnectionFactory();
        producer.setChannel(channel);
        producer.setDataGenerator(new DynamicDataGenerator());
        producer.setPersistenceStrategy(new NullObjectPersistenceStrategy("",""));


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    producer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        thread.start();

        Thread.sleep(2500);

        assertTrue(thread.isAlive());
    }


    @Test
    public void sendMessageWithDesiredPayload() throws InterruptedException {
        String queue_name = "TEST";
        RabbitMQChannel channel = new StandardQueue(queue_name);
        Producer producer = new Client(HOST,PORT,USER,PASSSWORD,30000);
        producer.factory = new MockConnectionFactory();
        producer.setChannel(channel);
        producer.setDataGenerator(new DynamicDataGenerator());
        producer.setPersistenceStrategy(new NullObjectPersistenceStrategy("",""));


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    producer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        thread.start();

        Thread.sleep(2500);

        assertTrue(thread.isAlive());
    }



    @Test
    public void RecoveryTest() throws IOException {




    }


}










