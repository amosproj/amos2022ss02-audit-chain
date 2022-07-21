package ProducerDummy.ChannelSelection;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class QuorumQueues extends RabbitMQChannel {

    public QuorumQueues(String name) {
        super(name);
        this.durable = true;
    }

    public QuorumQueues(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        super(name, durable, exclusive, autoDelete);
        if (durable) {
        } else {
            throw new RuntimeException("a QuorumQueues must be durable");
        }
    }


    @Override
    public Channel createChannel(ConnectionFactory factory) throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        com.rabbitmq.client.Channel channel = connection.createChannel();
        channel.queueDeclare(this.channel_name, this.durable, this.exclusive, this.autoDelete, Map.of("x-queue-type", "quorum"));
        channel.confirmSelect();
        return channel;
    }

}
