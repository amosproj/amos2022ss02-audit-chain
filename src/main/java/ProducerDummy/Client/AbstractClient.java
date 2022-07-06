package ProducerDummy.Client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import ProducerDummy.ChannelSelection.RabbitMQChannel;
import ProducerDummy.Persistence.PersistenceStrategy;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Abstract class for Clients
 */
abstract public class AbstractClient {

    protected String HOST;
    protected int PORT;
    protected String USER;
    protected String PASSWORD;
    public ConnectionFactory factory = new ConnectionFactory();
    // A wrapped RabbitMQ Channel. See RabbitMQChannel Folder.
    public RabbitMQChannel channel = null;
    // A Component which stores a Message persistently. See Persistence Folder
    protected PersistenceStrategy persistenceStrategy;
    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public AbstractClient(String host, int port, String username, String password) {
        this.HOST = host;
        this.PORT = port;
        this.USER = username;
        this.PASSWORD = password;
        this.initFactory();
    }

    /**
     * Initializes the connection factory. It can be used then to send messages to a server RabbitMQ
     */
    public void initFactory() {

        this.factory.setHost(this.HOST);
        this.factory.setUsername(this.USER);
        this.factory.setPassword(this.PASSWORD);
        this.factory.setPort(this.PORT);

    }

    public void start() throws IOException, TimeoutException, InterruptedException {
    }

    public void setChannel(RabbitMQChannel channel) {
        this.channel = channel;
    }

    public void setPersistenceStrategy(PersistenceStrategy persistenceStrategy) {
        this.persistenceStrategy = persistenceStrategy;
    }

}
