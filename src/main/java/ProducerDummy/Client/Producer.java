package ProducerDummy.Client;

import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.Messages.Message;
import ProducerDummy.Persistence.PersistenceStrategy;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Producer extends AbstractClient {

    /**
     * Every Producer must be able to store their Messages into a File and also must have a Datasource
     * */
    protected DataGenerator dataGenerator;
    protected PersistenceStrategy persistenceStrategy;


    static int START_NUMBER = 0;

    protected int sequence_number;

    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public Producer(String host, int port, String username, String password) {
        super(host, port, username, password);
    }

    public void setDataGenerator(DataGenerator dataGenerator){
        this.dataGenerator = dataGenerator;
    };

    public void setPersistenceStrategy(PersistenceStrategy persistenceStrategy){
     this.persistenceStrategy = persistenceStrategy;
    }


    public Channel getChannel() throws IOException, TimeoutException {

        return this.channel.createChannel(this.factory);
    }

    public void getAcknowledgment(Channel channel, ArrayList<Message> messageArrayList) {
        //we try for 5 times for acknowledgment and if we get it, we publish the message
        for (int i = 0; i <= 5; i++) {
            try {
                channel.basicPublish("", this.channel.getQueueName(), null, serialize(messageArrayList));
                channel.waitForConfirmsOrDie(5_000);
                break;
            } catch (InterruptedException | TimeoutException | IOException e) {
                if (i == 5) {
                    System.out.println("Could not send message");
                }
            }
        }
    }

    public boolean isReadyToSend() throws IOException {
        return true;

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
