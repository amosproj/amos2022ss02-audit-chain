package ProducerDummy.ChannelSelection;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ supports several sort of Queues. This Wrapper provides the ability to quickly Change the Queue.
 */
public abstract class RabbitMQChannel {
    protected String channel_name;
    protected boolean durable = false;
    protected boolean exclusive = false;
    protected boolean autoDelete = false;


    public RabbitMQChannel(String name) {
        this.channel_name = name;
    }

    public RabbitMQChannel(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        this.channel_name = name;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autoDelete = autoDelete;
    }


    public Channel createChannel(ConnectionFactory factory) throws IOException, TimeoutException {
        throw new RuntimeException("Not Implemented");
    }


    public String getQueueName() {
        return this.channel_name;
    }


}