package ConsumerDummy.Client;

import ProducerDummy.Messages.Message;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class StreamClient extends Consumer{

    private int current_offset = 0;
    // Since in most cases we want to receive a certain amount of messages/bytes we store them in a member while waiting for new ones to arrive
    protected ArrayList<Message> received_messages;
    protected int current_payload = 0;

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

    public StreamClient(String host, int port, String username, String password, int gui_port, String key, String algorithm) {
        super(host, port, username, password,gui_port,key,algorithm);
    }

        public void start() throws IOException, TimeoutException {
        this.RecoverOffset();
        Channel channel = this.getChannel();
        channel.basicQos(1);
        channel.basicConsume(
                this.channel.getQueueName(),
                false,
                Collections.singletonMap("x-stream-offset", 0 ), // From which offset to read (= which Message)
                (consumerTag, delivery) -> {
                    ArrayList<Message> messages;
                    try {
                        messages =  this.consumeDelivery(delivery.getBody());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    this.BeforeACK(messages);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    this.AfterACK(messages);
                },
                consumerTag -> { });
        this.listen();
    }

    @Override
    public void BeforeACK(ArrayList<Message> messages){
        return;
    }


    public void RecoverOffset() {
        try{
            ArrayList<Message> messages = this.persistenceStrategy.ReadLastMessage();
            this.current_offset = messages.get(messages.size()-1).getSequence_number();
        }catch(Exception e){
            // no file means there is no offset
            this.current_offset = 0;
            return;
        }
        System.out.println(String.format("Restored Current Offset to: %d",this.current_offset));
    }


}
