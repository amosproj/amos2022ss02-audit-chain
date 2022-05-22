import DataGeneration.DataGenerator;
import DataGeneration.FileDataReader;
import Messages.AbstractMessage;
import Messages.AggregateJsonMessage;
import Persistence.AggregateMessages;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


import java.time.Duration;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AggregateClient extends AbstractClient {

    private final AggregateMessages persistenceStrategy;

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
        this.persistenceStrategy = new AggregateMessages(path,"messages.txt");
    }

    public void start() throws IOException, TimeoutException, InterruptedException {
            System.out.println("Starting to send Messages.Message to AMQP Host");
            // Here you can declare another Message Type
            AggregateJsonMessage message;
            try (Connection connection = this.factory.newConnection();
                 Channel channel = connection.createChannel()) {
                 channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    
            channel.confirmSelect();
            long start = System.nanoTime();
    
                for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {
                    this.persistenceStrategy.StoreMessage(this.sequence_number,line);
                    this.sequence_number +=1;
    
                    if(this.persistenceStrategy.isReadyToSend()){
                        AbstractMessage m = this.persistenceStrategy.ReadLastMessage();
                        message = (AggregateJsonMessage) m;
                        byte[] bytes = message.serializeMessage();
                        channel.basicPublish("", this.QUEUE_NAME, null, message.serializeMessage());
    
            channel.waitForConfirmsOrDie(5_000);
            //IOEXCPETION if a message get lost missing                    
    
    this.sequence_number +=1;
                        this.persistenceStrategy.cleanFile();
                        TimeUnit.SECONDS.sleep(5);
    
                    }else{
                        System.out.println("Append Message: " + line);
                    }
                }
                
            long end = System.nanoTime();
            System.out.format("Messages until the position " + (this.sequence_number + 1)  + " are sent correctly and "
                    + "received by the Queue in " + Duration.ofNanos(end - start).toMillis());

           }
      return;
    }
    }