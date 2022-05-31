package Client;


import DataGeneration.DataGenerator;
import DataGeneration.FileDataReader;
import Messages.JsonMessage;
import Messages.Message;
import Persistence.AggregateMessageFilePersistence;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AggregateClient extends AbstractClient {

    private final AggregateMessageFilePersistence persistenceStrategy;

    //TODO Parameterize and not as static
    private static final String path = Paths.get("ProducerDummy", "src","main").toString();
    private final DataGenerator dataGenerator;
    private int sequence_number = 0;


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


    public void start() throws IOException, TimeoutException {

        System.out.println("Starting to send Messages.Message to AMQP Host");
        try (Connection connection = this.factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.confirmSelect();
            int start_event = this.sequence_number;

            for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {

                this.persistenceStrategy.StoreMessage(new JsonMessage(this.sequence_number, line));
                if (this.persistenceStrategy.isReadyToSend()) {
                    this.getAcknowledgment(channel, this.persistenceStrategy.ReadLastMessage());
                    this.sequence_number += 1;
                    this.persistenceStrategy.cleanFile();
                    System.out.format(String.format("Message from event Number %d until %d were sent",start_event,this.sequence_number));
                    //at least for now we just catch it ...
                    try{
                        TimeUnit.SECONDS.sleep(5);
                    }catch (InterruptedException e){
                        System.out.println("Thread was interrupted");
                    }
                    start_event = this.sequence_number;

                } else {
                    System.out.println("Append Message: " + line);
                    this.sequence_number+=1;
                }
            }
        }
        return;
    }


    public void getAcknowledgment(Channel channel, Message message) {
        //we try for 5 times for acknowledgment and if we get it, we publish the message
        for (int i = 0; i <= 5; i++) {
            try {
                channel.basicPublish("", this.QUEUE_NAME, null, serialize(message));
                channel.waitForConfirmsOrDie(5_000);
                break;
            } catch (InterruptedException | TimeoutException | IOException e) {
                if(i == 5){
                    System.out.println("Could not send message");
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