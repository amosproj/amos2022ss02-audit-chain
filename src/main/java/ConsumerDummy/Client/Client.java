package ConsumerDummy.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import ProducerDummy.Messages.Message;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import BlockchainImplementation.Blockchain.BlockchainIntSequenceAPI;


/**
 * Consumerclient implementation
 */
public class Client extends Consumer {
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