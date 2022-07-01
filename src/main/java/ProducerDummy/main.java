package ProducerDummy;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import ProducerDummy.ChannelSelection.QuorumQueues;
import ProducerDummy.ChannelSelection.RabbitMQChannel;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.ChannelSelection.Stream;
import ProducerDummy.Client.*;
import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.DynamicDataGenerator;
import ProducerDummy.DataGeneration.FileDataReader;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;
import ProducerDummy.Persistence.PersistenceStrategy;

public class main {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {

        String filepath = Paths.get("src", "main", "resources","ProducerDummy").toString();
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

        String queue_name = "stream";
        // create components for Client
        DataGenerator dataGenerator = new DynamicDataGenerator();
        PersistenceStrategy filePersistenceStrategy = new AggregateMessageFilePersistence(base_path, "last_messages.txt");
        RabbitMQChannel channel = new Stream(queue_name);

        Producer client = new SecurityClient(HOST, PORT, USER, PASSWORD,KEY,ALGORITHM);
        client.setDataGenerator(dataGenerator);
        client.setPersistenceStrategy(filePersistenceStrategy);
        client.setChannel(channel);
        client.start();

        return;
    }

    public static byte[] serialize(Object object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(object);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}

