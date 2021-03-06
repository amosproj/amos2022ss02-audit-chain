package ConsumerDummy.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ProducerDummy.Client.AbstractClient;
import ProducerDummy.Messages.Hmac_Message;
import ProducerDummy.Messages.Message;
import com.rabbitmq.client.*;
import BlockchainImplementation.Blockchain.BlockchainIntSequenceAPI;
import org.json.JSONObject;

/**
 * Implementation of an AbstractClient which holds basic Functionality every Consumer (may) need.
 */
public class Consumer extends AbstractClient {

    protected int gui_port;
    protected String key = null;
    protected String algorithm = null;

    public Consumer(String host, int port, String username, String password, int gui_port) {
        super(host, port, username, password);
        this.gui_port = gui_port;
    }

    public Consumer(String host, int port, String username, String password, int gui_port, String key, String algorithm) {
        super(host, port, username, password);
        this.gui_port = gui_port;
        this.key = key;
        this.algorithm = algorithm;
    }


    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);

        return objStream.readObject();

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
                    ArrayList<Message> messages;
                    try {
                        messages = this.consumeDelivery(delivery.getBody());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    this.BeforeACK(messages);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    this.AfterACK(messages);
                },
                consumerTag -> {
                });


        this.listen();
    }

    public void listen() throws IOException {
        while (true) {
            ServerSocket serverSocket = new ServerSocket(this.gui_port);
            System.out.println("Accepting Connections now");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Placeholder for the Blockchain
            BlockchainIntSequenceAPI blockchain = new BlockchainIntSequenceAPI<>("src/test/resources/testOutput/", 50);
            while (true) {
                try {
                    //converting the String into an JSON object
                    JSONObject jsonObject = new JSONObject(reader.readLine());
                    System.out.println("Request: " + jsonObject.toString());
                    //switch case over 'command' field
                    switch (jsonObject.get("command").toString()) {
                        case "check_single_message":
                            final String check_single_message = blockchain.getTemperedMessageIfAnyAsString(Integer.parseInt(jsonObject.get("number").toString()));
                            jsonObject.append("check_single_message", check_single_message);
                            break;
                        case "check_message_interval":
                            final String check_message_interval = blockchain.getTemperedMessageIfAnyAsString(Integer.parseInt(jsonObject.get("start").toString()), Integer.parseInt(jsonObject.get("end").toString()));
                            jsonObject.append("check_message_interval", check_message_interval);
                            break;
                        case "get_statistics":
                            jsonObject.append("amountDataRecords", blockchain.getBytesSize());
                            jsonObject.append("amountFilesCreated", blockchain.getNumberOfFiles());
                            jsonObject.append("currentSize", blockchain.getSize());
                            break;
                    }

                    //send back JSOnObject
                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Answer: " + jsonObject.toString());
                    dOut.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    dOut.flush();
                } catch (SocketException e) {
                    System.out.println("Client disconnected");
                    serverSocket.close();
                    break;
                } catch (Exception e) {
                    System.out.println("Error inside the Blockchain");
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public ArrayList<Message> consumeDelivery(byte[] delivery) throws IOException, ClassNotFoundException {
        ArrayList<Message> messages = (ArrayList<Message>) Consumer.deserialize(delivery);
        for (Message message : messages) {
            if (message instanceof Hmac_Message) {
                if (key != null) {
                    if (((Hmac_Message) message).verifyMAC(this.algorithm, this.key)) {
                        System.out.println("Message Mac was verified and the result is: Message is valid");
                    } else {
                        System.out.println("Message Mac was verified and the result is: MAC does not fit to Message");
                    }
                }
                System.out.println(String.format("Nachricht erhalten:\nEvent:%d\nMessage:%s\nMAC:%s", message.getSequence_number(), message.getMessage(), ((Hmac_Message) message).getHmac()));
            } else if (message instanceof Message) {
                System.out.println(String.format("Nachricht erhalten:\nEvent:%d\nMessage:%s", message.getSequence_number(), message.getMessage()));
            }
        }
        return messages;
    }

    public void BeforeACK(ArrayList<Message> messages){
        // call this function if you want to do anything with the messages BEFORE sending the ACK that the Message was received
        return;
    }


    public void AfterACK(ArrayList<Message> messages){
        // call this function if you want to do anything with the messages AFTER sending the ACK that the Message was received
        return;
    }



}
