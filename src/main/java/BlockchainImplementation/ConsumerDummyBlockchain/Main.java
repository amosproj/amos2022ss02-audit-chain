package BlockchainImplementation.ConsumerDummyBlockchain;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import ProducerDummy.ChannelSelection.QuorumQueues;
import ProducerDummy.ChannelSelection.RabbitMQChannel;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.Client.AbstractClient;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {
        String filepath = Paths.get("src", "main", "resources","BlockchainImplementation").toString();
        String filename = "config.properties";

        Path config_path = Paths.get(System.getProperty("user.dir"), filepath, filename);
        Properties p = new Properties();
        FileReader reader = new FileReader(config_path.toString());
        p.load(reader);

        String HOST = p.getProperty("HOST");
        int PORT = Integer.parseInt(p.getProperty("PORT"));
        String USER = p.getProperty("USERNAME");
        String PASSWORD = p.getProperty("PASSWORD");

        String queue_name = p.getProperty("QUEUE_NAME");

        AbstractClient client = new ConsumerClientBlockchain(HOST,PORT,USER,PASSWORD, "src/test/resources/testOutput/", Long.MAX_VALUE);
        client.setChannel(new QuorumQueues(queue_name));
        try {
            client.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return;

    }

}
