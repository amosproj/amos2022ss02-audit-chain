package ConsumerDummy.Client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import ProducerDummy.Client.AbstractClient;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer extends AbstractClient {

    static String QUEUE_NAME = "WORKAROUND";
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
    public Consumer(String host, int port, String username, String password) {
        super(host, port, username, password);
    }

    public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);

        return objStream.readObject();
    }

    @Override
    public void initFactory() {
        this.factory.setHost(this.HOST);
        if(!HOST.equals("localhost")) {
            this.factory.setUsername(this.USER);
            this.factory.setPassword(this.PASSWORD);
        }
        this.factory.setPort(this.PORT);
    }

    public ConnectionFactory getFactory(){
        return this.factory;
    }

    public Channel generateChannel() throws IOException, TimeoutException {
        Connection connection = this.factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, Map.of("x-queue-type", "quorum"));
        return channel;
    }

    public DeliverCallback DeliveryCallback(Channel channel){
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        return deliverCallback;
    }

}
