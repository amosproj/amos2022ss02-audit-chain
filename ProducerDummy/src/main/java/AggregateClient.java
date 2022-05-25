import DataGeneration.DataGenerator;
import DataGeneration.FileDataReader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


import java.time.Duration;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AggregateClient extends AbstractClient {


    private static final String path = "\\ProducerDummy\\src\\main\\";
    private final DataGenerator dataGenerator;
    private int sequence_number = 0;

    /**
     * Constructor for Client.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param queue_name
     * @throws IOException if an I/O error occurs
     */
    public AggregateClient(String host, int port, String username, String password, String queue_name) throws IOException {
        super(host, port, username, password, queue_name);
        this.dataGenerator = new FileDataReader();
    }

    public void start() throws IOException, TimeoutException, InterruptedException {
            System.out.println("Starting to send Messages.Message to AMQP Host");
      return;
    }
    }