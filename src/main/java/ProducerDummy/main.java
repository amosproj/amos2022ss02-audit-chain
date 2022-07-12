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
import ProducerDummy.Persistence.FilePersistenceStrategy;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;

public class main {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {

        Path path = Paths.get(System.getProperty("user.dir"));
        String filepath = Paths.get("src", "main", "resources","ProducerDummy").toString();
        String filename = "config.properties";

        if(Paths.get(System.getProperty("user.dir"), filepath, filename).isAbsolute()){
            Path config_path = Paths.get(System.getProperty("user.dir"), filepath, filename);
            Properties p = new Properties();
            FileReader reader = new FileReader(config_path.toString());
            p.load(reader);
            String HOST = p.getProperty("HOST");
            int PORT = Integer.parseInt(p.getProperty("PORT"));
            String USER = p.getProperty("USERNAME");
            String PASSWORD = p.getProperty("PASSWORD");

            String base_path = Paths.get(System.getProperty("user.dir"), filepath).toString();
            String queue_name = p.getProperty("QUEUE_NAME");

            // create components for Client

            DataGenerator dataGenerator;
            String data_generator_type = p.getProperty("DATA-GENERATOR_TYPE");
            switch(data_generator_type){
                case "filedatereader" :
                    dataGenerator = new FileDataReader(base_path, "household_power_consumption.txt");
                    break;

                default:
                    dataGenerator = new DynamicDataGenerator();
                    break;
            }

            PersistenceStrategy filePersistenceStrategy;
            String persistence_strategy_type = p.getProperty("PERSISTENCE-STRATEGY_TYPE");

            switch(persistence_strategy_type){
                case "aggregate-message" :
                    filePersistenceStrategy = new AggregateMessageFilePersistence(base_path, "last_messages.txt");
                    break;

                case "file" :
                    filePersistenceStrategy = new FilePersistenceStrategy(base_path, "last_messages.txt");
                    break;

                default:
                    filePersistenceStrategy = new NullObjectPersistenceStrategy(base_path, "last_messages.txt");
                    break;
            }

            RabbitMQChannel channel;
            String queue_type = p.getProperty("QUEUE_TYPE");
            switch(queue_type){
                case "stream" :
                    int stream_size = Integer.parseInt(p.getProperty("MAXIMUM_STREAM_SIZE"));
                    int segment_size = Integer.parseInt(p.getProperty("STREAM_SEGMENT_SIZE"));
                    channel = new Stream(queue_name,stream_size,segment_size);
                    break;

                case "quorum" :
                    channel = new QuorumQueues(queue_name);
                    break;

                default:
                    channel = new StandardQueue(queue_name);
                    break;
            }


            Producer client;
            String client_type = p.getProperty("CLIENT_TYPE");
            int DESIRED_PAYLOAD_IN_BYTE = Integer.parseInt(p.getProperty("DESIRED_PAYLOAD_IN_BYTE"));
            switch(client_type){
                case "security_client":
                    String KEY = p.getProperty("KEY");
                    String ALGORITHM = p.getProperty("ALGORITHM");
                    client = new SecurityClient(HOST, PORT, USER, PASSWORD, KEY, ALGORITHM, DESIRED_PAYLOAD_IN_BYTE);
                    break;

                default:
                    client = new Client(HOST, PORT, USER, PASSWORD, DESIRED_PAYLOAD_IN_BYTE);
                    break;
            }

            client.setDataGenerator(dataGenerator);
            client.setPersistenceStrategy(filePersistenceStrategy);
            client.setChannel(channel);
            client.start();

            return;

        }

    }

}