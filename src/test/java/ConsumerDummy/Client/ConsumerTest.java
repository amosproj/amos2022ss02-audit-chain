package ConsumerDummy.Client;

import ProducerDummy.Messages.*;

import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.util.Vector;
import java.util.concurrent.TimeoutException;


import static org.junit.jupiter.api.Assertions.*;

class ConsumerTest {

    @Test
    void initFactory() {
        Consumer c = new Consumer("localhost", 5672, "shouldn't be", "set in factory",9999);
        assertAll(
                () -> assertEquals("localhost", c.factory.getHost()),
                () -> assertEquals(5672, c.factory.getPort()),
                () -> assertEquals("guest", c.factory.getUsername()),  //should be guest in localhost
                () -> assertEquals("guest", c.factory.getPassword())); //should be guest in localhost
    }


    @Test
    void deserialize() throws IOException, ClassNotFoundException {
        SimpleMessage m = new SimpleMessage(1, "Test 12345"); // generate AggregateMessage

        byte[] bytecode = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // 64 - 71 serialize object
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(m);
            bytecode = out.toByteArray();
        } catch (IOException e) {
        }
        assertNotNull(bytecode); // check if serialization worked
        Consumer consumer = new Consumer("",9,"","",123);
        SimpleMessage mDeserialized = (SimpleMessage) consumer.deserialize(bytecode); //deserialization
        assertEquals(mDeserialized.getMessage(), "Test 12345");
    }

    @Test
    void getChannel() throws IOException, TimeoutException {
        Consumer c = new Consumer("localhost", 5672, "guest", "guest",9999);
        assertNull(c.getChannel());
    }

    @Test
    void start() throws IOException, TimeoutException {
        Consumer c = new Consumer("localhost", 5672, "guest", "guest",9999);
        assertThrows(NullPointerException.class, () -> c.start());
    }

    @Test
    void listen() throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            public void run() {
                Consumer c = new Consumer("localhost", 5672, "guest", "guest",9999);
                try {
                    c.listen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t.start();
        t.join(200);
        assertTrue(t.isAlive());
    }

    @Test
    void consumeDelivery() {
        Consumer c = new Consumer("localhost", 5672, "guest", "guest",9999);
        assertThrows(NullPointerException.class, () -> c.consumeDelivery(null));
    }

    @Test
    void beforeACK() {
        Consumer c = new Consumer("localhost", 5672, "guest", "guest",9999);
        assertDoesNotThrow(() -> c.BeforeACK(null));
    }

    @Test
    void afterACK() {
        Consumer c = new Consumer("localhost", 5672, "guest", "guest",9999);
        assertDoesNotThrow(() -> c.AfterACK(null));
    }

}