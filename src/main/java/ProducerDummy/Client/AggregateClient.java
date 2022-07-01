package ProducerDummy.Client;

import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import ProducerDummy.DataGeneration.NullObjectDataReader;
import ProducerDummy.Messages.AggregateMessage;
import ProducerDummy.Messages.Hmac_JsonMessage;
import ProducerDummy.Messages.Hmac_Message;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;


/***
 *
 * This Branch:
 * combines AggregateClient and Client
 * changes isReadyToSend to Client since it should be his job
 *
 */

public class AggregateClient extends Producer {

    private int SIZE = 2014;

    //TODO only for test purpose
    private static String KEY = "0123456";
    private static String ALGORITHM = "HmacSHA256";

    /**
     * Constructor for Client.Client.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param queue_name
     * @throws IOException if an I/O error occurs
     */
    public AggregateClient(String host, int port, String username, String password, String queue_name) throws IOException {
        super(host, port, username, password, queue_name);
        this.dataGenerator = new NullObjectDataReader();
        this.persistenceStrategy = new NullObjectPersistenceStrategy("no_path", "no_file");
    }

    public void start() throws IOException, TimeoutException {
        
        AggregateMessage aggregateMessage = new AggregateMessage();

        System.out.println("Starting to send Messages.Message to AMQP Host");

        try (Connection connection = this.factory.newConnection();
             Channel channel = connection.createChannel()) {

                channel.queueDeclare(QUEUE_NAME, true, false, false, Map.of("x-queue-type", "quorum"));
                channel.confirmSelect();

                int start_event = this.sequence_number;

                for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {

                    Hmac_Message hmac_message = new Hmac_JsonMessage(this.sequence_number,line,ALGORITHM,KEY);
                    aggregateMessage.addMessage(hmac_message);
                    this.persistenceStrategy.StoreMessage(hmac_message);

                    if (isReadyToSend()) {
                        this.getAcknowledgment(channel, this.persistenceStrategy.ReadLastMessage());
                        this.sequence_number += 1;
                        this.persistenceStrategy.cleanFile();
                        System.out.format(String.format("Message from event Number %d until %d were sent  \n", start_event, this.sequence_number));
                        
                        //at least for now we just catch it ...
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            System.out.println("Thread was interrupted");
                        }

                        start_event = this.sequence_number;
                        
                    } else {
                        System.out.println("Append Message: " + line);
                        this.sequence_number += 1;
                    }
                }

        }catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return;
    }

    public boolean isReadyToSend() throws IOException {

        long bytes = Files.size(this.persistenceStrategy.getFilePath());
        
        if (bytes <= this.SIZE) {
            return false;
        } else {
            return true;
        }
    }

    public int getSequence_number() {
        return this.sequence_number;
    }

}
