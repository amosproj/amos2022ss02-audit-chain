package ProducerDummy.Client;

import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.Messages.Message;
import ProducerDummy.Persistence.PersistenceStrategy;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
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
     * @param queue_name
     */
    public Producer(String host, int port, String username, String password, String queue_name) {
        super(host, port, username, password, queue_name);
    }

    public void setDataGenerator(DataGenerator dataGenerator){
        this.dataGenerator = dataGenerator;
    };

    public void setPersistenceStrategy(PersistenceStrategy persistenceStrategy){
     this.persistenceStrategy = persistenceStrategy;
     recoverLastState();
    }

    public void recoverLastState() {
        Message message;
        try {
            message = this.persistenceStrategy.ReadLastMessage();
            this.sequence_number = message.getSequence_number();
        } catch (Exception e) {
            this.sequence_number = START_NUMBER;
            return;
        }
        this.dataGenerator.getData(this.sequence_number);
        // Message(s) in a file will be sent instantly
    }


    public Channel getChannel() throws IOException, TimeoutException {
        Connection connection = this.factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //channel.queueDeclare(QUEUE_NAME, true, false, false, Map.of("x-queue-type", "quorum"));
        /*
        channel.queueDeclare(
                QUEUE_NAME,
                true,         // durable
                false, false, // not exclusive, not auto-delete
                Map.of(
                        "x-queue-type", "stream",
                        "x-max-length-bytes", 1_000_000_000, // maximum stream size: 20 GB
                        "x-stream-max-segment-size-bytes", 1_000_000 // size of segment files: 100 MB
                )
        );
        */
        return channel;
    }

    private void sendRecoveredMessage(Message message) {
        System.out.println("Message(s) in File found, will send them immediately!");
        try (Connection connection = this.factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, Map.of("x-queue-type", "quorum"));
            channel.confirmSelect();
            this.getAcknowledgment(channel, message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        this.persistenceStrategy.cleanFile();
    }





    public void getAcknowledgment(Channel channel, Message message) {
        //we try for 5 times for acknowledgment and if we get it, we publish the message
        for (int i = 0; i <= 5; i++) {
            try {
                channel.basicPublish("", this.QUEUE_NAME, null, serialize(message));
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
