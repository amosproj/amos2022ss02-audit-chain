import ConsumerDummy.Client.Consumer;
import ConsumerDummy.Client.StreamClient;
import ProducerDummy.ChannelSelection.QuorumQueues;
import ProducerDummy.ChannelSelection.RabbitMQChannel;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.ChannelSelection.Stream;
import ProducerDummy.Client.Client;
import ProducerDummy.Client.Producer;
import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.DynamicDataGenerator;
import ProducerDummy.DataGeneration.FileDataReader;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
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
        String queue_name = "HERE2";

        RabbitMQChannel producer_channel = new QuorumQueues(queue_name);
        RabbitMQChannel consumer_channel = new QuorumQueues(queue_name);

        DataGenerator dataGenerator = new FileDataReader(System.getProperty("user.dir")+ "\\src\\main\\java\\ProducerDummy","household_power_consumption.txt");
        PersistenceStrategy persistenceStrategy = new NullObjectPersistenceStrategy("","",2000);



        ConsumerDummy.Client.Consumer consumer = new ConsumerDummy.Client.Client(HOST,PORT,USER,PASSWORD);
        ProducerDummy.Client.Producer producer = new Client(HOST,PORT,USER,PASSWORD);

        producer.setDataGenerator(dataGenerator);
        producer.setPersistenceStrategy(persistenceStrategy);
        consumer.setPersistenceStrategy(new NullObjectPersistenceStrategy("",""));
        producer.setChannel(producer_channel);
        consumer.setChannel(consumer_channel);


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




        return;
    }

}