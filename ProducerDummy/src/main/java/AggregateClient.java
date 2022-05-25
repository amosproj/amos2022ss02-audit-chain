import DataGeneration.DataGenerator;
import DataGeneration.FileDataReader;
import Messages.AggregateMessage;
import Messages.JsonMessage;
import Messages.Message;
import Persistence.AggregateMessageFilePersistence;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AggregateClient extends AbstractClient {

    private final AggregateMessageFilePersistence persistenceStrategy;
    private int sequence_number = START_NUMBER;

    private static final String path = "\\ProducerDummy\\src\\main\\";
    private final DataGenerator dataGenerator;

    /**
     * Constructor for Client.
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
        this.dataGenerator = new FileDataReader();
        this.persistenceStrategy = new AggregateMessageFilePersistence(path, "messages.txt");
        this.recoverLastState();
    }


    public void recoverLastState(){
        try {
            this.sequence_number = this.persistenceStrategy.ReadLastMessage().getSequence_number() +1;
        }catch (ArrayIndexOutOfBoundsException e){
            return;
        }
        this.dataGenerator.getData(this.sequence_number);
    }

    public void start() throws IOException, TimeoutException, InterruptedException {
        System.out.println("Starting to send Messages.Message to AMQP Host");

        try (Connection connection = this.factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.confirmSelect();

            long start = System.nanoTime();
            for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {

                this.persistenceStrategy.StoreMessage(new JsonMessage(this.sequence_number, line));

                if (this.persistenceStrategy.isReadyToSend()) {
                    AggregateMessage message = (AggregateMessage) this.persistenceStrategy.ReadLastMessage();
                    channel.basicPublish("", this.QUEUE_NAME, null, serialize(message));
                    //channel.waitForConfirmsOrDie(5_000);
                    //wait for its confirmation with the Channel#waitForConfirmsOrDie(long) method
                    //IOEXCPETION is thrown if a message is lost
                    this.persistenceStrategy.cleanFile();
                    TimeUnit.SECONDS.sleep(5);

                } else {
                    System.out.println("Append Message: " + line);
                }
                this.sequence_number += 1;
            }

            long end = System.nanoTime();
            System.out.format("Messages until the position " + (+1) + " are sent correctly and "
                    + "received by the Queue in " + Duration.ofNanos(end - start).toMillis());

        }
        return;
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
