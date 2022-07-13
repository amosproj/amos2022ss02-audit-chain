package ConsumerDummy;

import ConsumerDummy.Client.Client;
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
import ProducerDummy.Client.Producer;
import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.DynamicDataGenerator;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;
import ProducerDummy.Persistence.FilePersistenceStrategy;
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
        int gui_port = 6868;

        PersistenceStrategy filePersistenceStrategy = new NullObjectPersistenceStrategy("","");
        RabbitMQChannel channel = new Stream(queue_name);

        /**
        Consumer consumer;
        PersistenceStrategy filePersistenceStrategy;
        RabbitMQChannel channel;

        consumer = new StreamClient("",9,"","",9); // steam client is only for Queue=Stream
        consumer = new Client("",9,"","",9); //stand queue and qurom queue
        PersistenceStrategy persistenceStrategy;

        persistenceStrategy = new AggregateMessageFilePersistence("",""); // I don´t know if we actually need it
        persistenceStrategy = new FilePersistenceStrategy("",""); // used in Stream
        persistenceStrategy = new NullObjectPersistenceStrategy("",""); // standard PersitenceStrategy

        RabbitMQChannel rabbitMQChannel;
        rabbitMQChannel = new Stream("46556465"); // ONLY WORKS WITH STREAMCLIENT
        rabbitMQChannel = new QuorumQueues("46556465"); // NORMAL CLIENT
        rabbitMQChannel = new StandardQueue("46556465"); // NORMAL CLIENT

        consumer.setPersistenceStrategy(persistenceStrategy);
        consumer.setChannel(rabbitMQChannel);

        String QUEUE_TYPE = null;
        String CLIENT_TYPE = null;

        if(!(QUEUE_TYPE.equals("STREAM") && CLIENT_TYPE.equals("STREAMCLIENT"))){
            System.out.println("Stream need a StreamClient");
            return;
        }

        if(  (QUEUE_TYPE.equals("STAND_QUEUE") || QUEUE_TYPE.equals("QUROU_QUE")) && CLIENT_TYPE.equals("CLIENT") ){
            System.out.println("Normal Client only works with Standard_que or quroum_que");
            return;
        }
**/
        //same with persistence strategy
        Consumer client = new StreamClient(HOST, PORT, USER, PASSWORD,gui_port);
        client.setPersistenceStrategy(filePersistenceStrategy);
        client.setChannel(channel);
        client.start();

        return;
    }

}