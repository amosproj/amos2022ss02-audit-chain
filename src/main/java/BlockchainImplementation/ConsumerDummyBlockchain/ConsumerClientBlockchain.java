package BlockchainImplementation.ConsumerDummyBlockchain;

import BlockchainImplementation.Blockchain.Blockchain;
import ProducerDummy.Client.AbstractClient;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ConsumerClientBlockchain extends AbstractClient {

    private Blockchain blockchain = new Blockchain(1);

    /**
     * Constructor for AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public ConsumerClientBlockchain() throws IOException {
        super("localhost",5672,"guest","guest","ConsumerDummyBlockchain");
    }

    /***
     * Start receiving Messages from the RabbitMQ Server.
     * @throws IOException if an I/O error occurs
     * @throws TimeoutException if the timeout expires
     */
    public void start() throws IOException, TimeoutException {
        System.out.println("Starting to receive Messages.");
        Connection connection = this.factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println(" [*] Waiting for messages.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");

            Map jsonJavaRootObject = new Gson().fromJson(message, Map.class);

            //is it message and sequence_number the name on the JSON??
            String msg = (String) jsonJavaRootObject.get("message");
            String sqz = (String) jsonJavaRootObject.get("sequence_number");

           // blockchain.addABlock(sqz, msg);

            //blockchain.printBlockchain();
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}

