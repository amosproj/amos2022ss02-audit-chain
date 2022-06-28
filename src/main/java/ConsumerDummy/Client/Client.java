package ConsumerDummy.Client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;


/**
 * Consumerclient implementation
 */
public class Client extends Consumer {
    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public Client(String host, int port, String username, String password, String queue_name) throws IOException {
        super(host, port, username, password, queue_name);
    }



    /***
     * Start receiving Messages from the RabbitMQ Server.
     * @throws IOException if an I/O error occurs
     * @throws TimeoutException if the timeout expires
     */
    public void start() throws IOException, TimeoutException {
        System.out.println("Starting to receive Messages.");
        Channel channel = generateChannel();
        System.out.println(" [*] Waiting for messages.");

        DeliverCallback deliverCallback = DeliveryCallback(null);
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }


}

