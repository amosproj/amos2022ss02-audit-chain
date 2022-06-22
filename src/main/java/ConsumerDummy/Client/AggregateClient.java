package ConsumerDummy.Client;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import ProducerDummy.Messages.JsonMessage;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;

public class AggregateClient extends Consumer {

    static int sequence_number = 0;
    private final AggregateMessageFilePersistence persistenceStrategy;

    //TODO Parameterize
    private static final String path = Paths.get("src", "main", "resources","ConsumerDummy").toString();

    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public AggregateClient(String host, int port, String username, String password, String queue_name) throws IOException {
        super(host, port, username, password, queue_name);
        this.persistenceStrategy = new AggregateMessageFilePersistence(path, "messages.txt");
    }

    public void start() throws IOException, TimeoutException {
        System.out.println("Starting to receive Messages.");

        Connection connection = this.factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, Map.of("x-queue-type", "quorum"));

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            ProducerDummy.Messages.AggregateMessage message;
            try {
                message = (ProducerDummy.Messages.AggregateMessage) deserialize(delivery.getBody()); // cast deserialized bytestream to AggregateMessage
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            Vector<ProducerDummy.Messages.Message> messages = message.getMessages(); // get all the messages out of the AggregateMessage
            for (int i = 0; i < message.getMessageSize(); i++) { //add each message out of messages to persistence storage
                ProducerDummy.Messages.Message single_message = messages.get(i);
                this.persistenceStrategy.StoreMessage(new JsonMessage(single_message.getSequence_number(), single_message.getMessage())); //add massage to the file
                System.out.println(String.format("Received event %d with the content: %s", single_message.getSequence_number(), single_message.getMessage()));
            }

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
    }

}





