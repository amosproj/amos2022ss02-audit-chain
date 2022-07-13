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
    public Client(String host, int port, String username, String password) throws IOException {
        super(host, port, username, password);
    }

    public static void main(String [] args) throws IOException{
    
            // TODO bind ip else it just works on the pc 0.0.0.0:6868
            ServerSocket serverSocket = new ServerSocket(6868);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine();

                System.out.println(line); 
             }
        
    }
}