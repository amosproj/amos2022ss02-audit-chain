package Producer;

import ProducerDummy.ChannelSelection.QuorumQueues;
import ProducerDummy.ChannelSelection.RabbitMQChannel;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.ChannelSelection.Stream;
import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class RabbitMqChannelTest {

    @Test
    public void createStreamChannel() throws IOException, TimeoutException {
        String queue_name = "TEST";
        RabbitMQChannel channel = new Stream(queue_name,100000,10000);
        channel.createChannel(new MockConnectionFactory());
    }


    @Test
    public void createStandardChannel() throws IOException, TimeoutException {
        String queue_name = "TEST";
        RabbitMQChannel channel = new StandardQueue(queue_name);
        channel.createChannel(new MockConnectionFactory());
        channel = new StandardQueue(queue_name,false,false,false);
        channel.createChannel(new MockConnectionFactory());
    }

    @Test
    public void createQuoruQueueChannel() throws IOException, TimeoutException {
        String queue_name = "TEST";
        RabbitMQChannel channel = new QuorumQueues(queue_name);
        channel.createChannel(new MockConnectionFactory());
    }


    @Test
    public void GetChannelNameTest() throws IOException, TimeoutException {
        String queue_name = "TEST";
        RabbitMQChannel channel = new QuorumQueues(queue_name);
        channel.createChannel(new MockConnectionFactory());
        assertEquals(channel.getQueueName(),queue_name);
    }

    @Test
    public void CreateChannelConstructor() throws IOException, TimeoutException {
        String queue_name = "TEST";
        RabbitMQChannel channel = new QuorumQueues(queue_name,true,true,true);
        channel.createChannel(new MockConnectionFactory());
        assertEquals(channel.getQueueName(),queue_name);
        channel.createChannel(new MockConnectionFactory());


    }

    @Test(expected = RuntimeException.class)
    public void createQuoruQueueChannelWrong() throws IOException, TimeoutException {
        String queue_name = "TEST";
        RabbitMQChannel channel = new QuorumQueues(queue_name,false,true,true);
        channel.createChannel(new MockConnectionFactory());
        assertEquals(channel.getQueueName(),queue_name);
    }



}
