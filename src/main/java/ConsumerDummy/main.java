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
import org.apache.commons.cli.*;


public class main {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {

        Options options = new Options();
        Option host = new Option( "h","host", false, "host ip");
        host.setRequired(false);
        options.addOption(host);
        Option port = new Option("p","port", false, "port");
        port.setRequired(false);
        options.addOption(port);
        Option username = new Option("u","username", true, "username of rabbitmq");
        username.setRequired(false);
        options.addOption(username);
        Option password = new Option("pw","password", true, "password of rabbitmq");
        password.setRequired(false);
        options.addOption(password);

        CommandLine cmd;
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();

        String HOST = null, PORT = null, USER = null, PASSWORD = null;

        try {
            cmd = parser.parse(options, args);
            HOST = cmd.getOptionValue("host");
            PORT = cmd.getOptionValue("port");
            USER = cmd.getOptionValue("username");
            PASSWORD = cmd.getOptionValue("password");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("java -jar target/AuditChain-ConsumerDummy.jar --host 127.0.0.1 --port 5672 --username admin --password admin", options);
            System.exit(1);
        }

        String filepath = Paths.get("src", "main", "resources","ConsumerDummy").toString();
        String filename = "config.properties"; // in current folder or in ressources is a valid place for this file
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
        if (HOST == null) {
            HOST = p.getProperty("HOST");
        }
        if (PORT == null) {
            PORT = p.getProperty("PORT");
        }
        if (USER == null) {
            USER = p.getProperty("USERNAME");
        }
        if (PASSWORD == null) {
            PASSWORD = p.getProperty("PASSWORD");
        }
        String QUEUE_TYPE = p.getProperty("QUEUE_TYPE");
        String PERSISTENCE_STRATEGY_TYPE = p.getProperty("PERSISTENCE-STRATEGY_TYPE");
        String KEY = p.getProperty("KEY");
        String ALGORITHM = p.getProperty("ALGORITHM");
        String QUEUE_NAME = p.getProperty("QUEUE_NAME");
        String CLIENT_TYPE = p.getProperty("CLIENT_TYPE");
        int GUI_PORT = Integer.parseInt(p.getProperty("GUI_PORT"));
        //
        Consumer consumer;
        PersistenceStrategy filePersistenceStrategy;
        RabbitMQChannel channel;


        switch (CLIENT_TYPE){
            case "standard":
                if(KEY != null && ALGORITHM != null){
                    consumer = new Client(HOST,Integer.parseInt(PORT),USER,PASSWORD,GUI_PORT,KEY,ALGORITHM);
                }else{
                    consumer = new Client(HOST,Integer.parseInt(PORT),USER,PASSWORD,GUI_PORT);
                }

                break;
            case "stream":
                if(KEY != null && ALGORITHM != null){
                    consumer = new StreamClient(HOST,Integer.parseInt(PORT),USER,PASSWORD,GUI_PORT,KEY,ALGORITHM);
                }else{
                    consumer = new StreamClient(HOST,Integer.parseInt(PORT),USER,PASSWORD,GUI_PORT);
                }
                break;
            default:
                throw new RuntimeException("No Valid Client Value selected.");
        }

        switch (QUEUE_TYPE){
            case "standard":
                channel = new StandardQueue(QUEUE_NAME);
                break;
            case "stream":
                channel = new Stream(QUEUE_NAME);
                break;
            case "quorum":
                channel = new QuorumQueues(QUEUE_NAME);
                break;
            default:
                throw new RuntimeException("No Valid Channel Value selected.");
        }

        switch (PERSISTENCE_STRATEGY_TYPE){
            case "aggregate-message":
                filePersistenceStrategy = new AggregateMessageFilePersistence(base_path,"consumer_last_messages.txt");
                break;
            case "file":
                filePersistenceStrategy = new FilePersistenceStrategy(base_path,"consumer_last_message.txt");
                break;
            case "nullobject":
                filePersistenceStrategy = new NullObjectPersistenceStrategy(base_path,filename);
                break;
            default:
                throw new RuntimeException("No Valid Persistence Strategy Value selected.");
        }

        consumer.setPersistenceStrategy(filePersistenceStrategy);
        consumer.setChannel(channel);
        consumer.start();

        return;
    }

}