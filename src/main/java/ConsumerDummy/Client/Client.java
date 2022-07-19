package ConsumerDummy.Client;

import java.io.IOException;



/**
 * Consumerclient implementation
 */
public class  Client extends Consumer {
    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public Client(String host, int port, String username, String password,int gui_port) throws IOException {
        super(host, port, username, password,gui_port);
    }

    public Client(String host, int port, String username, String password, int gui_port, String key, String algorithm) {
        super(host, port, username, password,gui_port,key,algorithm);
    }



}