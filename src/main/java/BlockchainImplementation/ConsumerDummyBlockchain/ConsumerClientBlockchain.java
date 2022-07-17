package BlockchainImplementation.ConsumerDummyBlockchain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import BlockchainImplementation.Blockchain.BlockchainIntSequence;
import ProducerDummy.Messages.SimpleMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import ConsumerDummy.Client.Consumer;
import ProducerDummy.Messages.Hmac_Message;
import ProducerDummy.Messages.Message;

import static java.nio.file.StandardOpenOption.*;

public class ConsumerClientBlockchain extends Consumer {

    private BlockchainIntSequence<String> blockchain;
    private static String KEY = "0123456";
    private static String ALGORITHM = "HmacSHA256";

    private String path;

    /**
     * Constructor for AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public ConsumerClientBlockchain(String host, int port, String username, String password, String path, long maxSizeByte,int gui_port) throws IOException {

        super(host, port, username, password,gui_port);
        this.blockchain = new BlockchainIntSequence<>(path, maxSizeByte);
        this.path = path;
    }

    /***
     * Start receiving Messages from the RabbitMQ Server.
     * @throws IOException if an I/O error occurs
     * @throws TimeoutException if the timeout expires
     */
    @Override
    public void start() throws IOException, TimeoutException {
        System.out.println("Starting to receive Messages.");
        Channel channel = this.getChannel();
        System.out.println(" [-] Waiting for messages.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            try {
                ArrayList<Message> messages = this.consumeDelivery(delivery.getBody());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        };
        channel.basicConsume(this.channel.getQueueName(), true, deliverCallback, consumerTag -> {
        });
    }

    @Override
    public ArrayList<Message> consumeDelivery(byte[] delivery) throws IOException, ClassNotFoundException {

        ArrayList<Message> messages = null;

        try {
            messages = (ArrayList<Message>) Consumer.deserialize(delivery);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Integer[] seq_numbers = new Integer[messages.size()];
        String[] transactions = new String[messages.size()];
        int iterator = 0;


        for (Message m : messages) {

            if (m instanceof Hmac_Message) {
                Hmac_Message hmac_message = (Hmac_Message) m;

                if (!hmac_message.verifyMAC(ALGORITHM, KEY)) {
                    // store it in a file Message HMAC is wrong: tempered messages

                    String content = "Timestamp: " + System.currentTimeMillis() + "\n";
                    content += hmac_message + "\n";

                    Path path1 = Paths.get(path + "/LogTemperedMessage.txt");
                    Files.writeString(path1, content, StandardCharsets.UTF_8, CREATE, APPEND);

                    throw new RuntimeException("Authentication of the message failed!");
                }
            }

            seq_numbers[iterator] = m.getSequence_number();
            transactions[iterator] = m.getMessage();
            iterator++;
            System.out.println(" [x] Received n." + m.getSequence_number() + " - '" + m.getMessage() + "'");
        }

        blockchain.addABlock(seq_numbers, transactions);
        System.out.println(" [---] Added a new block to the blockchain with previous messages [---]");
        System.out.println(" [-] Waiting for messages.");


        return messages;
    }


}

