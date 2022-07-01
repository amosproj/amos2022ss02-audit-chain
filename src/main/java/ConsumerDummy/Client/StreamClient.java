package ConsumerDummy.Client;

import ProducerDummy.Messages.Message;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class StreamClient extends Consumer{


    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @throws IOException if the file cannot be read
     */
    public StreamClient(String host, int port, String username, String password) throws IOException {
        super(host, port, username, password);
    }

    public void start() throws IOException, TimeoutException {
        // create Callback to receive Messages
        Channel channel = this.getChannel();
        channel.basicQos(1);

        channel.basicConsume(
                this.channel.getQueueName(),
                false,
                Collections.singletonMap("x-stream-offset", "first"), // "first" offset specification
                (consumerTag, delivery) -> {
                    try {
                        ArrayList<Message> messages = (ArrayList<Message>) Consumer.deserialize(delivery.getBody());
                        messages.forEach(message ->
                        System.out.println(String.format(" [%d] Received %s'", message.getSequence_number(), message.getMessage())));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                },
                consumerTag -> { });
        this.listen();
    }

}
