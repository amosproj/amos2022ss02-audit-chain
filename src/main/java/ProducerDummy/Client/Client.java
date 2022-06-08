package ProducerDummy.Client;

import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.FileDataReader;
import ProducerDummy.DataGeneration.NullObjectDataReader;
import ProducerDummy.Messages.JsonMessage;
import ProducerDummy.Messages.Message;
import ProducerDummy.Persistence.FilePersistenceStrategy;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Client.Client implementation
 */
public class Client extends AbstractClient {

    private DataGenerator dataGenerator;
    private PersistenceStrategy persistenceStrategy;
    /**
     * Sequence number kept by the client to order the message it sends
     */

    private int sequence_number = START_NUMBER;

    private final static int SECOND_DELAY_BETWEEN_MESSAGES = 5;

    /**
     * Constructor for Client.Client.
     *
     * @throws IOException if an I/O error occurs
     */
    public Client(String host, int port, String username, String password, String queue_name) throws IOException {
        super(host, port, username, password, queue_name);
        this.dataGenerator = new NullObjectDataReader();
        this.persistenceStrategy = new NullObjectPersistenceStrategy("nopath","nofile");
        this.recoverLastState();
        this.recoverLastMessage();
    }


    public void setDataGenerator(DataGenerator dataGenerator){
        this.dataGenerator = dataGenerator;
    }

    public void setPersistenceStrategy(PersistenceStrategy persistenceStrategy){
        this.persistenceStrategy = persistenceStrategy;
    }


    /**
     * Recover the last message stored in the persistence mechanism and use its sequence number to set the
     * DataGenerator again to the point it was before "the interruption"
     */
    public void recoverLastState(){
        Message lastMessage = this.persistenceStrategy.ReadLastMessage();
        if(lastMessage != null) {
            this.sequence_number = lastMessage.getSequence_number();
        }
    }

    public Message recoverLastMessage(){
        Message lastMessage = this.persistenceStrategy.ReadLastMessage();
        if(lastMessage != null) {
            return lastMessage;
        }
        return null;
    }



    /***
     * Start Sending Messages as JSON to the RabbitMQ Server.
     * TODO sending Messages is still bad and just a minimal example make it better
     * @throws IOException if an I/O error occurs
     * @throws TimeoutException if the timeout expires
     * @throws InterruptedException if the thread is interrupted
     */
    public void start() throws IOException, TimeoutException {

        System.out.println("Starting to send Messages.Message to AMQP Host");
        try (Connection connection = this.factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // Recover last Message which was stored in file and maybe was not send
            Message message = this.recoverLastMessage();
            if(message != null){
                channel.basicPublish("", QUEUE_NAME, null, serialize(message));
                this.sequence_number +=1;
            }

            for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {
                System.out.println("The following Messages.Message will be send:\n" + line);

                message = new JsonMessage(this.sequence_number,line);
                this.persistenceStrategy.StoreMessage(message);
                channel.basicPublish("", QUEUE_NAME, null, serialize(message));
                this.sequence_number +=1;
                try {
                    TimeUnit.SECONDS.sleep(SECOND_DELAY_BETWEEN_MESSAGES);
                } catch (InterruptedException e) {
                    System.out.println("Thread was interrupted");
                }
            }
        }
    }



    public static byte[] serialize(Object object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(object);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}


