package ConsumerDummy.Client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import ProducerDummy.Client.AbstractClient;

public class Consumer extends AbstractClient {

    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param queue_name
     * @throws IOException if the file cannot be read
     */
    public Consumer(String host, int port, String username, String password, String queue_name) {
        super(host, port, username, password, queue_name);
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

}
