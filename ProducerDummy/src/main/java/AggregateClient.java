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

    private static final String path = "\\ProducerDummy\\src\\main\\";
    private final DataGenerator dataGenerator;
    private int sequence_number = 0;

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
        this.persistenceStrategy = new AggregateMessageFilePersistence(path,"messages.txt");
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
                if(this.persistenceStrategy.isReadyToSend()){
                    getAcknowledgment(channel, line, serialize(this.persistenceStrategy.ReadLastMessage()));
                    this.sequence_number +=1;
                    this.persistenceStrategy.cleanFile();
                    TimeUnit.SECONDS.sleep(5);
                }else{
                    System.out.println("Append Message: " + line);
                }
            }
            long end = System.nanoTime();
            System.out.format("Messages until the position " + (this.sequence_number)  + " are sent correctly and "
                    + "received by the Queue in " + Duration.ofNanos(end - start).toMillis());
        }
        return;
    }

    
    
    
    
    
    public void getAcknowledgment(Channel channel, String line, byte[] serialized_message) {
        //we try for 5 times for aknowledgment and if we get it, we publish the message
        for (int i = 0; i <= 5; i++) {
            try {
                channel.basicPublish("", this.QUEUE_NAME, null, serialized_message);
                channel.waitForConfirmsOrDie(5_000);
                break;
                //IOEXCPETION if a message get lost missing                    
            } catch (InterruptedException | TimeoutException | IOException e) {
                getAcknowledgment(channel, line, serialized_message);
            }
        }
    }


        public static byte[] serialize(Object object){
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