package ProducerDummy.ChannelSelection;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Stream extends RabbitMQChannel {

    int MAXIMUM_STREAM_SIZE = 1_000_000_000; // 1 GB
    int STREAM_SEGMENT_SIZE = 1_000_000; // 100 MB

    public Stream(String name) {
        super(name);
        this.setStreamProperties();
    }

    public Stream(String name, int stream_size, int segment_size) {
        super(name);
        this.MAXIMUM_STREAM_SIZE = stream_size;
        this.STREAM_SEGMENT_SIZE = segment_size;
        this.setStreamProperties();
    }


    private void setStreamProperties() {
        this.durable = true;
        this.autoDelete = false;
        this.exclusive = false;
    }


    public Channel createChannel(ConnectionFactory factory) throws IOException, TimeoutException {

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(
                this.channel_name,
                this.durable, // MUST BE true
                this.exclusive, // MUST BE not exclusive
                this.autoDelete,
                Map.of(
                        "x-queue-type", "stream",
                        "x-max-length-bytes", this.MAXIMUM_STREAM_SIZE, // maximum stream size: 20 GB
                        "x-stream-max-segment-size-bytes", this.STREAM_SEGMENT_SIZE // size of segment files: 100 MB
                )
        );
        channel.confirmSelect();
        return channel;


    }


}
