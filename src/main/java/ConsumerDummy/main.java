package ConsumerDummy;

import ConsumerDummy.Client.Client;
import ConsumerDummy.Client.Consumer;
import ConsumerDummy.Client.StreamClient;
import ProducerDummy.ChannelSelection.QuorumQueues;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.ChannelSelection.Stream;
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
        String HOST = null, PORT = null, USER = null, PASSWORD = null;
        Consumer consumer = null;

        if (StartViaCommandLine(args)) {
        } else {
            StartViaFile();
        }

        return;
    }

    public static boolean StartViaCommandLine(String[] args) throws IOException, TimeoutException {
        Consumer consumer = null;
        if (args.length != 0) {
            String HOST = null, PORT = null, USER = null, PASSWORD = null, GUI_PORT = null;

            Options options = new Options();
            Option host = new Option("h", "host", true, "host ip");
            host.setRequired(false);
            options.addOption(host);
            Option port = new Option("p", "port", true, "port");
            port.setRequired(false);
            options.addOption(port);
            Option username = new Option("u", "username", true, "username of rabbitmq");
            username.setRequired(false);
            options.addOption(username);
            Option password = new Option("pw", "password", true, "password of rabbitmq");
            password.setRequired(false);
            options.addOption(password);

            CommandLine cmd;
            HelpFormatter formatter = new HelpFormatter();
            CommandLineParser parser = new DefaultParser();
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
            // Consumer was started via command line
            consumer = new Consumer(HOST, Integer.parseInt(PORT), USER, PASSWORD, 45212);
            // random channel name I guess
            consumer.setChannel(new StandardQueue("default_queue_"+String.valueOf((int)(Math.random()*100))));
            consumer.start();
            return true;
        } else {
            // Consumer had 0 args therefore initialize with file
            return false;
        }


    }


    public static void StartViaFile() throws IOException, TimeoutException {
        Consumer consumer = null;
        PersistenceStrategy persistenceStrategy = null;
        String HOST = null, PORT = null, USER = null, PASSWORD = null;
        // Command Line args was zero therefore use File
        String filepath = Paths.get("src", "main", "resources", "ConsumerDummy").toString();
        String filename = "config.properties"; // in current folder or in ressources is a valid place for this file
        Path config_path = Paths.get(System.getProperty("user.dir"), filepath, filename);
        String base_path = Paths.get(System.getProperty("user.dir")).toString();

        FileReader reader;
        //file should be either in the resources folder of the Consumer or in the current folder.
        try {
            reader = new FileReader(config_path.toString());
        } catch (IOException e) {
            //if we could not found it this folder we check the current one
            reader = new FileReader(Paths.get(System.getProperty("user.dir"), filename).toString());
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
        int GUI_PORT = Integer.parseInt(p.getProperty("GUI_PORT"));
        PersistenceStrategy filePersistenceStrategy;


        switch (QUEUE_TYPE) {
            case "standard":
                consumer = new Client(HOST, Integer.parseInt(PORT), USER, PASSWORD, GUI_PORT, KEY, ALGORITHM);
                consumer.setChannel(new StandardQueue(QUEUE_NAME));
                break;
            case "quorum":
                consumer = new Client(HOST, Integer.parseInt(PORT), USER, PASSWORD, GUI_PORT, KEY, ALGORITHM);
                consumer.setChannel(new QuorumQueues(QUEUE_NAME));
                break;
            case "stream":
                consumer = new StreamClient(HOST, Integer.parseInt(PORT), USER, PASSWORD, GUI_PORT, KEY, ALGORITHM);
                consumer.setChannel(new Stream(QUEUE_NAME));
                break;
            default:
                throw new RuntimeException("No Valid Channel Value selected.");
        }

        switch (PERSISTENCE_STRATEGY_TYPE) {
            case "aggregate-message":
                filePersistenceStrategy = new AggregateMessageFilePersistence(base_path, "consumer_last_messages.txt");
                break;
            case "file":
                filePersistenceStrategy = new FilePersistenceStrategy(base_path, "consumer_last_message.txt");
                break;
            case "nullobject":
                filePersistenceStrategy = new NullObjectPersistenceStrategy(base_path, filename);
                break;
            default:
                throw new RuntimeException("No Valid Persistence Strategy Value selected.");
        }

        consumer.setPersistenceStrategy(filePersistenceStrategy);
        consumer.start();

        return;


    }
}