package ProducerDummy.Client;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import ProducerDummy.DataGeneration.NullObjectDataReader;
import ProducerDummy.Messages.JsonMessage;
import ProducerDummy.Messages.Message;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;

/**
 * Client.Client implementation
 */
public class Client extends Producer {

    private final static int SECOND_DELAY_BETWEEN_MESSAGES = 5;

    public Client(String host, int port, String username, String password, String queue_name) throws IOException {

        super(host, port, username, password, queue_name);
        this.dataGenerator = new NullObjectDataReader();
        this.persistenceStrategy = new NullObjectPersistenceStrategy("nopath","nofile");

    }

    /**
     * Recover the last message stored in the persistence mechanism and use its sequence number to set the
     * DataGenerator again to the point it was before "the interruption"
     */

    public Message recoverLastMessage(){

        Message lastMessage = this.persistenceStrategy.ReadLastMessage();
        if(lastMessage != null) {
            return lastMessage;
        }
        return null;

    }



    /***
     * Start Sending Messages to the RabbitMQ Server.
     * @throws IOException if an I/O error occurs
     * @throws TimeoutException if the timeout expires
     * @throws InterruptedException if the thread is interrupted
     */
    public void start() throws IOException, TimeoutException {

        System.out.println("Starting to send Messages.Message to AMQP Host");

        try (Connection connection = this.factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, Map.of("x-queue-type", "quorum"));

            // If sequence_number is bigger we have a Message in our persistence_mechanism
            if(this.sequence_number > START_NUMBER){
                Message message = this.recoverLastMessage();
                channel.basicPublish("", QUEUE_NAME, null, serialize(message));
                this.sequence_number +=1;
            }

            for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {

                System.out.println("The following Messages.Message will be send:\n" + line);

                Message message = new JsonMessage(this.sequence_number,line);
                this.persistenceStrategy.StoreMessage(message);
                channel.basicPublish("", QUEUE_NAME, null, serialize(message.toSimpleFormat()));
                this.sequence_number +=1;
                
                try {
                    TimeUnit.SECONDS.sleep(SECOND_DELAY_BETWEEN_MESSAGES);
                } catch (InterruptedException e) {
                    System.out.println("Thread was interrupted");
                }
            }
        }
    }

}


