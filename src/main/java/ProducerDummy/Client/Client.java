package ProducerDummy.Client;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ProducerDummy.Messages.SimpleMessage;
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
        //TODO simply let the filepersistence return a vector with the messages, if a file exists
        Vector<Message> messageVector = new Vector<>();

        System.out.println("Starting to send Messages.Message to AMQP Host");
        Channel channel = this.getChannel();
        for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {
            /***
             * Create the Message, store it into the PersistenceMechanism and add it to the vector.
             * Now decide weather to send the vector or not.
             * Strategy are either to send every Message as one or collect a certain amount of messages and send them later
             */

            Message message = new SimpleMessage(sequence_number,line);
            this.persistenceStrategy.StoreMessage(message);
            messageVector.add(message);
            if(isReadyToSend()){
                //TODO change to messagevector
                this.getAcknowledgment(channel,message);
                this.persistenceStrategy.cleanFile();

            }else{
                System.out.println(String.format("Append Message with event Nr %d: ",this.sequence_number ) + line);
            }
            this.sequence_number++;

        }

        channel.close();

        }


}


