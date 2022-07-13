package ConsumerDummy;

import ConsumerDummy.Client.Consumer;
import ConsumerDummy.Client.StreamClient;
//import ConsumerDummy.Client.Client;
//import ConsumerDummy.DataGeneration.DataGenerator;
//import ConsumerDummy.DataGeneration.DynamicDataGenerator;
import ProducerDummy.ChannelSelection.QuorumQueues;
import ProducerDummy.ChannelSelection.RabbitMQChannel;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.ChannelSelection.Stream;
import ProducerDummy.Client.AbstractClient;
import ProducerDummy.Client.Client;
import ProducerDummy.Client.Producer;
import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.DynamicDataGenerator;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;



public class main {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {
        String filepath = Paths.get("src", "main", "resources","ConsumerDummy").toString();
        String filename = "config.properties";

        Path config_path = Paths.get(System.getProperty("user.dir"), filepath, filename);
        Properties p = new Properties();
        FileReader reader = new FileReader(config_path.toString());
        p.load(reader);
        String HOST = p.getProperty("HOST");
        int PORT = Integer.parseInt(p.getProperty("PORT"));
        String USER = p.getProperty("USERNAME");
        String PASSWORD = p.getProperty("PASSWORD");
        String KEY = "0123456";
        String ALGORITHM = "HmacSHA256";
        String base_path = Paths.get(System.getProperty("user.dir"), filepath).toString();
        String queue_name = "TEST";



        PersistenceStrategy filePersistenceStrategy = new NullObjectPersistenceStrategy(base_path, "last_messages.txt");
        RabbitMQChannel channel = new Stream(queue_name);


        Consumer client = new StreamClient(HOST, PORT, USER, PASSWORD);
        client.setPersistenceStrategy(filePersistenceStrategy);
        client.setChannel(channel);
        client.start();

        return;
    }

}