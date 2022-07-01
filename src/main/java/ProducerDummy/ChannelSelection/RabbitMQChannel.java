package ProducerDummy.ChannelSelection;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class RabbitMQChannel{
    protected String channel_name;

    public RabbitMQChannel(String name){
        this.channel_name = name;
    }

    public Channel createChannel(ConnectionFactory factory) throws IOException, TimeoutException {
            throw new RuntimeException("Not Implemented");
    }


    public String getQueueName(){
        return this.channel_name;
    }



}