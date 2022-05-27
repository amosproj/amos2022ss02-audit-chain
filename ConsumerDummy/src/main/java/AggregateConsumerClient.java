
import Messages.AggregateMessage;
import Messages.JsonMessage;
import Messages.Message;
import Persistence.AggregateMessageFilePersistence;
import Persistence.PersistenceStrategy;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import Client.AbstractClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

public class AggregateConsumerClient extends AbstractClient {

    static int sequence_number = 0;
    private AggregateMessageFilePersistence persistenceStrategy;

    //TODO Parameterize
    private static final String path = Paths.get("ConsumerDummy", "src", "main").toString();


    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public AggregateConsumerClient(String host, int port, String username, String password, String queue_name) throws IOException {
        super(host, port, username, password, queue_name);
        this.persistenceStrategy = new AggregateMessageFilePersistence(path, "messages.txt");
    }

    public void start() throws IOException, TimeoutException {

        System.out.println("Starting to receive Messages.");

        Connection connection = this.factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AggregateMessage message;
            try {
                message = (AggregateMessage) deserializeMessage(delivery.getBody());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            Vector<Message> messages = message.getMessages();
            for (int i = 0; i < message.getMessageSize(); i++) {
                Message single_message = messages.get(i);
                this.persistenceStrategy.StoreMessage(new JsonMessage(single_message.getSequence_number(), single_message.getMessage()));
                System.out.println(String.format("Received event %d with the content: %s", single_message.getSequence_number(), single_message.getMessage()));
            }

        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }


    public static Object deserializeMessage(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);

        return objStream.readObject();
    }


}





