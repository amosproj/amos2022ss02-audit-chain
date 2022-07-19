package ConsumerDummy.Client;

import ProducerDummy.ChannelSelection.RabbitMQChannel;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.ChannelSelection.Stream;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {


    String HOST;
    int PORT;
    String USER;
    String PASSSWORD;
    String QUEUE;
    int GUI_PORT = 40400;

    @Before
    public void setUp() throws IOException {

        HOST = "127.0.0.1";
        PORT = 9999;
        USER = "user";
        PASSSWORD = "admin";
        QUEUE = "FAKE";
    }


    @Test
    @DisplayName("Testing constructor")
    void constructor() {
        try {
            Client c = new Client("localhost", 5672, "shouldn't be", "set in factory", GUI_PORT);
            assertNotNull(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Testing constructor")
    void constructorLong() {
        try {
            Client c = new Client("localhost", 5672, "shouldn't be", "set in factory", 0, "key", "algorithm");
            assertNotNull(c);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    void ClientStartTest() throws IOException, InterruptedException {

        String queue_name = "TEST";
        RabbitMQChannel channel = new StandardQueue(queue_name);
        Consumer consumer = new Client(HOST, PORT, USER, PASSSWORD, GUI_PORT+1);
        consumer.factory = new MockConnectionFactory();
        consumer.setChannel(channel);

        consumer.setPersistenceStrategy(new NullObjectPersistenceStrategy("", ""));

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    consumer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        thread.start();

        Thread.sleep(2500);

        assertTrue(thread.isAlive());

    }

    @Test
    void StramClientStartTest() throws IOException, InterruptedException {

        String queue_name = "TEST";
        RabbitMQChannel channel = new Stream(queue_name);
        Consumer consumer = new Client(HOST, PORT, USER, PASSSWORD, 30000);
        consumer.factory = new MockConnectionFactory();
        consumer.setChannel(channel);

        consumer.setPersistenceStrategy(new NullObjectPersistenceStrategy("", ""));

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    consumer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        thread.start();

        Thread.sleep(2500);

        assertTrue(thread.isAlive());

    }




}