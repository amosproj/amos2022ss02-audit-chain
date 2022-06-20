package BlockchainImplementation.ConsumerDummyBlockchain;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import BlockchainImplementation.Blockchain.Blockchain;
import ConsumerDummy.Client.Consumer;
import ProducerDummy.Messages.AggregateMessage;
import ProducerDummy.Messages.Hmac_Message;
import ProducerDummy.Messages.Message;

public class ConsumerClientBlockchain extends Consumer {

    private Blockchain<Integer, String> blockchain;
    private static String KEY = "0123456";
    private static String ALGORITHM = "HmacSHA256";

    /**
     * Constructor for AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public ConsumerClientBlockchain(String host, int port, String username, String password, String queue_name) throws IOException {

        super(host, port, username, password, queue_name);
        this.blockchain = new Blockchain<>();

    }

    /***
     * Start receiving Messages from the RabbitMQ Server.
     * @throws IOException if an I/O error occurs
     * @throws TimeoutException if the timeout expires
     */
    @Override
    public void start() throws IOException, TimeoutException {
        System.out.println("Starting to receive Messages.");
        Connection connection = this.factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println(" [-] Waiting for messages.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AggregateMessage message;

            try {
                message = (AggregateMessage) deserialize(delivery.getBody());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            Vector<Message> messages = message.getMessages();

            Integer[] seq_numbers = new Integer[messages.size()];
            String[] transactions = new String[messages.size()];
            int iterator = 0;

            for (Message m : messages) {

                if (m instanceof Hmac_Message) {
                    Hmac_Message hmac_message = (Hmac_Message) m;

                    if (!hmac_message.verifyMAC(ALGORITHM, KEY)) {
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
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }

}

