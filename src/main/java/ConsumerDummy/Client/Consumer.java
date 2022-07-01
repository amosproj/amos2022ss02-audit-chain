package ConsumerDummy.Client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import ProducerDummy.Client.AbstractClient;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer extends AbstractClient {

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
    protected PersistenceStrategy persistenceStrategy = new NullObjectPersistenceStrategy("","");

    public Consumer(String host, int port, String username, String password) {
        super(host, port, username, password);
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);

        return objStream.readObject();

    }

    public void setPersistenceStrategy(PersistenceStrategy persistenceStrategy){
        this.persistenceStrategy = persistenceStrategy;
    }


    public ConnectionFactory getFactory(){
        return this.factory;
    }

    public Channel getChannel() throws IOException, TimeoutException {
        return this.channel.createChannel(this.factory);
    }

    public DeliverCallback DeliveryCallback(Channel channel){
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        return deliverCallback;
    }

}
