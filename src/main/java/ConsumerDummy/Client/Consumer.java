package ConsumerDummy.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ProducerDummy.Client.AbstractClient;
import ProducerDummy.Messages.Message;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import BlockchainImplementation.Blockchain.BlockchainIntSequenceAPI;

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

    public Consumer(String host, int port, String username, String password) {
        super(host, port, username, password);
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);

        return objStream.readObject();

    }

    public ConnectionFactory getFactory(){
        return this.factory;
    }

    public Channel getChannel() throws IOException, TimeoutException {
        return this.channel.createChannel(this.factory);
    }



    /***
     * Simple Consumer, receives Messages, prints them out and delivers an ACK
     * Start receiving Messages from the RabbitMQ Server.
     * @throws IOException if an I/O error occurs
     * @throws TimeoutException if the timeout expires
     */
    public void start() throws IOException, TimeoutException {
        // create Callback to receive Messages
        Channel channel = this.channel.createChannel(this.factory);
        channel.basicConsume(
                this.channel.getQueueName(), // set Queue Name
                false, // Autoack no
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

    public void listen() throws IOException {
        ServerSocket serverSocket = new ServerSocket(6868);
        while(true){
            try {
                System.out.println("Accepting Connections now");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                byte[] buffer = new byte[1024]; // a read buffer of 5KiB
                byte[] redData;
                StringBuilder clientData = new StringBuilder();
                String redDataText;
                int data;
                StringBuilder message_from_client = new StringBuilder();
                while ((data = socket.getInputStream().read(buffer)) > -1) {
                    redData = new byte[data];
                    System.arraycopy(buffer, 0, redData, 0, data);
                    redDataText = new String(redData,"UTF-8"); // data ist UTF-8 encoded
                    if(redDataText.contains("END")){
                        String message_to_be_interpreted = message_from_client.toString();
                        System.out.println("message recieved:" + message_to_be_interpreted);
                        OutputStream output = socket.getOutputStream();
                        PrintWriter writer = new PrintWriter(output);
                        writer.println(message_to_be_interpreted);
                        writer.flush();
                        message_from_client = new StringBuilder();
                        //TODO here is your part, create the methods to communicate with the Blockchain. Communicate with Francesco about it
                    }else{
                        message_from_client.append(redDataText);
                    }
                }
            }
            catch(SocketException e){
             System.out.println("Client disconnected");
            }
        }
    }

}
