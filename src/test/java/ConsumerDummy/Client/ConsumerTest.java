package ConsumerDummy.Client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerTest {

    @Test
    void initFactory() {
        Consumer c = new Consumer("localhost", 5672, "shouldn't be", "set in factory",9999);
        ConnectionFactory factory = c.getFactory();
        assertAll(
                () -> assertEquals("localhost", c.factory.getHost()),
                () -> assertEquals(5672, c.factory.getPort()),
                () -> assertEquals("guest", c.factory.getUsername()),  //should be guest in localhost
                () -> assertEquals("guest", c.factory.getPassword())); //should be guest in localhost
    }

    @Test
    void generateChannel() throws IOException, TimeoutException {
        Consumer c = new Consumer("localhost", 5672, "shouldn't be", "set in factory",9999);
        //TODO fixe me
        Channel channel = null;

        assertAll(
                () -> assertEquals(1, channel.getChannelNumber()),
                () -> assertNull(channel.getDefaultConsumer()),
                () -> assertNotNull(channel.getConnection()));
    }

    @Test
    void deliveryCallback() {
        Consumer c = new Consumer("localhost", 5672, "shouldn't be", "set in factory",9999 );
        DeliverCallback deliverCallback = null;
        //TODO fixme
        //        deliverCallback = c.DeliveryCallback(null);
        assertNotNull(deliverCallback);
    }

}