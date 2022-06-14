package ProducerDummy.Client;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Abstract class for Clients
 */
abstract public class AbstractClient {

    protected String HOST;
    protected int PORT;
    protected String USER;
    protected String PASSWORD;
    protected String QUEUE_NAME;
    public ConnectionFactory factory = new ConnectionFactory();

    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public AbstractClient(String host, int port, String username, String password, String queue_name) {
        this.HOST = host;
        this.PORT = port;
        this.USER = username;
        this.PASSWORD = password;
        this.QUEUE_NAME = queue_name;
        this.initFactory();
    }

    /**
     * Initializes the connection factory. It can be used then to send messages to a server RabbitMQ
     */
    public void initFactory() {
        this.factory.setHost(this.HOST);
        if(!HOST.equals("localhost")) {
            this.factory.setUsername(this.USER);
            this.factory.setPassword(this.PASSWORD);
        }
        this.factory.setPort(this.PORT);
    }


    public void start() throws IOException, TimeoutException {

    }

    public void recoverLastState() {
        return;
    }


}
