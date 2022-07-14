package ConsumerDummy.Client;

import ProducerDummy.Messages.Message;
import ProducerDummy.Messages.SimpleMessage;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class StreamClient extends Consumer{

    private int current_offset = 0;

    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @throws IOException if the file cannot be read
     */
    public StreamClient(String host, int port, String username, String password, int gui_port) throws IOException {
        super(host, port, username, password,gui_port);
    }
    public void start() throws IOException, TimeoutException {
        this.RecoverOffset();
        Channel channel = this.getChannel();
        channel.basicQos(1);
        channel.basicConsume(
                this.channel.getQueueName(),
                false,
                Collections.singletonMap("x-stream-offset", this.current_offset ), // From which offset to read (= which Message)
                (consumerTag, delivery) -> {
                    try {
                        ArrayList<Message> messages =  this.consumeDelivery(delivery.getBody());


                        this.persistenceStrategy.StoreMessage(new SimpleMessage(this.current_offset++,"Current Offset"));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                },
                consumerTag -> { });
        this.listen();
    }


    public void RecoverOffset() {
        try{
            ArrayList<Message> messages = this.persistenceStrategy.ReadLastMessage();
            this.current_offset = messages.get(messages.size()-1).getSequence_number();
        }catch(Exception e){
            System.out.println("Could not restore Consumers last Offset, starting from Zero");
            // if something goes wrong we start from the beginning
            this.current_offset = 0;
        }
    }


}
