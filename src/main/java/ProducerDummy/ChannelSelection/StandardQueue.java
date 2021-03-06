package ProducerDummy.ChannelSelection;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class StandardQueue extends RabbitMQChannel{
    public StandardQueue(String name) {
        super(name);
    }

    public StandardQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        super(name,durable,exclusive,autoDelete);
    }


    public Channel createChannel(ConnectionFactory factory) throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        com.rabbitmq.client.Channel channel = connection.createChannel();
        channel.queueDeclare(this.channel_name, this.durable, this.exclusive, this.autoDelete, null);
        channel.confirmSelect();
        return channel;
    }

}
