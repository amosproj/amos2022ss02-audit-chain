import DataGeneration.FileDataReader;
import Messages.AbstractMessage;
import Messages.JsonMeesage;
import Persistence.FilePersistenceStrategy;
import Persistence.NullObjectPersistenceStrategy;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Client extends AbstractClient {
    private int sequence_number = 0;
    private String message = null;
    private final static int SECOND_DELAY_BETWEEN_MESSAGES = 5;


    public Client() throws IOException {
        super();
        this.dataGenerator = new FileDataReader();
        this.persistenceStrategy = new NullObjectPersistenceStrategy();
        this.RecoverLastMessage();
    }


    public void RecoverLastMessage() {
        // get the Last Messages.Message
        AbstractMessage message = this.persistenceStrategy.ReadLastMessage();
        // If Messages.Message is null we can assume the file is empty. Use value of dataGenerator
        if (message == null) {
            this.sequence_number = 0;
            this.message = null;
            return;
        }
        this.sequence_number = message.getSequenceNumber();
        this.message = message.getMessage_string();
        // set buffered reader of data-generator to the current line
        // maybe this.sequence_number - 1 ?
        this.dataGenerator.getData(this.sequence_number);


    }


    /***
     * Start Sending Messages to the RabbitMQ Server
     * TODO sending Messages is still bad and just a minimal example make it better
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public void start() throws IOException, TimeoutException, InterruptedException {
        System.out.println("Starting to send Messages.Message to AMQP Host");
        // Here you can declare another Message Type
        JsonMeesage message;
        try (Connection connection = this.factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //before we send new messages we send the last message which was stored in file
            if (this.message != null) {
                message = new JsonMeesage(this.sequence_number, this.message);
                channel.basicPublish("", QUEUE_NAME, null, message.serializeMessage());
            }
            // TODO more config

            for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {
                System.out.println("The following Messages.Message will be send:\n" + line);
                message = new JsonMeesage(this.sequence_number, line);
                channel.basicPublish("", QUEUE_NAME, null, message.serializeMessage());
                this.persistenceStrategy.StoreMessage(this.sequence_number, line);
                this.sequence_number++;
                TimeUnit.SECONDS.sleep(SECOND_DELAY_BETWEEN_MESSAGES);
            }
        }
    }

}

