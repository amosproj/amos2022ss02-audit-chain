import ConsumerDummy.Client.Consumer;
import ConsumerDummy.Client.StreamClient;
import ProducerDummy.ChannelSelection.QuorumQueues;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.ChannelSelection.Stream;
import ProducerDummy.Client.Client;
import ProducerDummy.Client.Producer;
import ProducerDummy.Client.SecurityClient;
import ProducerDummy.DataGeneration.DynamicDataGenerator;
import ProducerDummy.DataGeneration.FileDataReader;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;
import ProducerDummy.Persistence.FilePersistenceStrategy;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class main {
    /***
     * Start Consumer and Producer in different Threads with same Arguments
     *
     *
     * @param args
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        String filepath = Paths.get("src", "main", "resources").toString();
        String filename = "config.properties"; // in current folder or in resources is a valid place for this file
        Path config_path = Paths.get(System.getProperty("user.dir"), filepath, filename);
        String base_path = Paths.get(System.getProperty("user.dir")).toString();

        FileReader reader;
        try{
            reader = new FileReader(config_path.toString());
        }catch (IOException e){
            //if we could not found it this folder we check the current one
            reader = new FileReader(Paths.get(System.getProperty("user.dir"),filename).toString());
        }
        Properties p = new Properties();
        p.load(reader);
        // get values out of the file
        String HOST = p.getProperty("HOST");
        int PORT = Integer.parseInt(p.getProperty("PORT"));
        String USER = p.getProperty("USERNAME");
        String PASSWORD = p.getProperty("PASSWORD");
        String QUEUE_TYPE = p.getProperty("QUEUE_TYPE");
        String PERSISTENCE_STRATEGY_TYPE = p.getProperty("PERSISTENCE-STRATEGY_TYPE");
        String DATA_GENERATOR_TYPE = p.getProperty("DATA-GENERATOR_TYPE");

        String KEY = p.getProperty("KEY");
        String ALGORITHM = p.getProperty("ALGORITHM");
        String QUEUE_NAME = p.getProperty("QUEUE_NAME");
        String CLIENT_TYPE = p.getProperty("CLIENT_TYPE");
        int GUI_PORT = Integer.parseInt(p.getProperty("GUI_PORT"));
        String DESIRED_PAYLOAD_IN_BYTE = p.getProperty("DESIRED_PAYLOAD_IN_BYTE");
        int STREAM_SIZE = Integer.parseInt(p.getProperty("MAXIMUM_STREAM_SIZE"));
        int SEGMENT_SIZE = Integer.parseInt(p.getProperty("STREAM_SEGMENT_SIZE"));
        //
        Consumer consumer;
        Producer producer;

// to be honest not the best solution, but don't want to rewrite it
        switch (CLIENT_TYPE){
            case "security_client":
                if(DESIRED_PAYLOAD_IN_BYTE != null){
                    producer = new SecurityClient(HOST,PORT,USER,PASSWORD,KEY,ALGORITHM,Integer.parseInt(DESIRED_PAYLOAD_IN_BYTE));
                }else{
                    producer = new SecurityClient(HOST,PORT,USER,PASSWORD,KEY,ALGORITHM);
                }
                // consumer part cares about Stream or queue that´s why check it in queue part
            break;
            case "client":
                if(DESIRED_PAYLOAD_IN_BYTE != null){
                    producer = new Client(HOST,PORT,USER,PASSWORD,Integer.parseInt(DESIRED_PAYLOAD_IN_BYTE));
                }else{
                    producer = new Client(HOST,PORT,USER,PASSWORD);
                }
                break;
            default:
                throw new RuntimeException("No Valid Client Value selected.");
        }




        switch (QUEUE_TYPE){
            case "standard":
                // if property Key/Algo does not exist it means that key is null and this case is covered in the consumer itself
                // and even if they exist but normal client was selected it does not matter, since Consumer will then receive only non Hmac Messages
                consumer = new ConsumerDummy.Client.Client(HOST,PORT,USER,PASSWORD,GUI_PORT,KEY,ALGORITHM);
                consumer.setChannel(new StandardQueue(QUEUE_NAME));
                producer.setChannel(new StandardQueue(QUEUE_NAME));
                break;
            case "quorum":
                consumer = new ConsumerDummy.Client.Client(HOST,PORT,USER,PASSWORD,GUI_PORT,KEY,ALGORITHM);
                consumer.setChannel(new QuorumQueues(QUEUE_NAME));
                producer.setChannel(new QuorumQueues(QUEUE_NAME));
                break;
            case "stream":
                consumer = new StreamClient(HOST,PORT,USER,PASSWORD,GUI_PORT,KEY,ALGORITHM);
                consumer.setChannel(new Stream(QUEUE_NAME));
                producer.setChannel(new Stream(QUEUE_NAME,STREAM_SIZE,SEGMENT_SIZE));
                break;
            default:
                throw new RuntimeException("No Valid Channel Value selected.");
        }


        switch (PERSISTENCE_STRATEGY_TYPE){
            case "aggregate-message":
                consumer.setPersistenceStrategy(new AggregateMessageFilePersistence(base_path,"consumer_last_messages.txt"));
                producer.setPersistenceStrategy(new AggregateMessageFilePersistence(base_path,"producer_last_messages.txt"));
                break;
            case "file":
                consumer.setPersistenceStrategy(new FilePersistenceStrategy(base_path,"consumer_last_message.txt"));
                producer.setPersistenceStrategy(new FilePersistenceStrategy(base_path,"producer_last_message.txt"));
                break;
            case "nullobject":
                consumer.setPersistenceStrategy(new NullObjectPersistenceStrategy(base_path,filename));
                producer.setPersistenceStrategy(new NullObjectPersistenceStrategy(base_path,filename));
                break;
            default:
                throw new RuntimeException("No Valid Persistence Strategy Value selected.");
        }

        switch (DATA_GENERATOR_TYPE) {
            case "filedatereader":
                producer.setDataGenerator(new FileDataReader(base_path, "household_power_consumption.txt"));
                break;
            default:
                producer.setDataGenerator(new DynamicDataGenerator());
        }



        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    producer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    consumer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();

    }

}
