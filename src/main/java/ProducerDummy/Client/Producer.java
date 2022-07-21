package ProducerDummy.Client;

import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.Messages.Message;
import com.rabbitmq.client.Channel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Producer extends AbstractClient {


    // Data Generator is the Component which generates the Data for the RabbitMQ. See DataGeneration Folder
    protected DataGenerator dataGenerator;
    protected int sequence_number;
    protected int DESIRED_PAYLOAD_IN_BYTE = 1024;
    protected int current_payload = 0;

    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     */
    // if you want to aggregate Messages and not sending every Message to RabbitMQ use this one. Be careful tho, the desired_payload_in_byte is just an estimation
    public Producer(String host, int port, String username, String password, int desired_payload_in_byte) {
        super(host, port, username, password);
        this.DESIRED_PAYLOAD_IN_BYTE = desired_payload_in_byte;
    }

    // A normal Producer which sends every Message to RabbitMQ
    public Producer(String host, int port, String username, String password) {
        super(host, port, username, password);
        this.DESIRED_PAYLOAD_IN_BYTE = 0; // we don´t care, we send every Message instantly
    }

    // Data Generator is the Component which generates the Data for the RabbitMQ. See DataGeneration Folder
    public void setDataGenerator(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
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
        if (DESIRED_PAYLOAD_IN_BYTE == 0) {
            return true;
        }
        if (this.current_payload >= DESIRED_PAYLOAD_IN_BYTE) {
            this.current_payload = 0; //reset
            return true;
        }
        return false;
    }

    protected void RecoverCurrentPayloadSize(ArrayList<Message> messages) {
        // if desired is 0 we can save the time
        if (DESIRED_PAYLOAD_IN_BYTE == 0) {
            return;
        }
        // if payload actually matters.
        for (Message m : messages) {
            this.updatePayloadSize(m);
        }
    }

    protected void updatePayloadSize(Message message) {
        //just an estimation, also overhead of object/arraylist does not matter
        this.current_payload += message.getPayloadSize();
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
