package ProducerDummy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.apache.commons.cli.*;

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

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
        if (StartViaCommandLine(args)) {
        } else {
            StartViaFile();
        }
    }
    public static boolean StartViaCommandLine(String[] args) throws IOException, InterruptedException, TimeoutException {
        Producer producer = null;

        String HOST = null, PORT = null, USER = null, PASSWORD = null;
        // if args is zero surely he did not want to use the commandline
        if (args.length != 0) {
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
            // if args is zero surely he did not want to use the commandline
            try {
                cmd = parser.parse(options, args);
                HOST = cmd.getOptionValue("host");
                PORT = cmd.getOptionValue("port");
                USER = cmd.getOptionValue("username");
                PASSWORD = cmd.getOptionValue("password");
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                formatter.printHelp("java -jar target/AuditChain-ProducerDummy.jar --host 127.0.0.1 --port 5672 --username admin --password admin", options);
                System.exit(1);

            }

            producer = new Client(HOST, Integer.parseInt(PORT), USER, PASSWORD);
            producer.setChannel(new StandardQueue("default_queue_"+String.valueOf((int)(Math.random()*100))));
            producer.start();
            return true;
        } else {
            return false;
        }

    }

    public static void StartViaFile() throws IOException, InterruptedException, TimeoutException {

        Producer client = null;
        String HOST = null, PORT = null, USER = null, PASSWORD = null;
        DataGenerator dataGenerator = null;
        PersistenceStrategy persistenceStrategy = null;
        // fall back to file
        String filepath = Paths.get("src", "main", "resources", "ProducerDummy").toString();
        String filename = "config.properties";
        Path config_path = Paths.get(System.getProperty("user.dir"), filepath, filename);

        FileReader reader;
        // for inteliji use ressources else the current folder for the jar
        try {
            reader = new FileReader(config_path.toString());
        } catch (IOException e) {
            //if we could not found it this folder we check the current one
            reader = new FileReader(Paths.get(System.getProperty("user.dir"), filename).toString());
        }

        Properties p = new Properties();
        p.load(reader);

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

        String base_path = Paths.get(System.getProperty("user.dir")).toString();
        String queue_name = p.getProperty("QUEUE_NAME");

        String data_generator_type = p.getProperty("DATA-GENERATOR_TYPE");
        switch (data_generator_type) {
            case "filedatereader":
                dataGenerator = new FileDataReader(base_path, "household_power_consumption.txt");
                break;

            default:
                dataGenerator = new DynamicDataGenerator();
                break;
        }

        String persistence_strategy_type = p.getProperty("PERSISTENCE-STRATEGY_TYPE");

        switch (persistence_strategy_type) {
            case "aggregate-message":
                persistenceStrategy = new AggregateMessageFilePersistence(base_path, "producer_last_messages.txt");
                break;

            case "file":
                persistenceStrategy = new FilePersistenceStrategy(base_path, "producer_last_message.txt");
                break;

            default:
                persistenceStrategy = new NullObjectPersistenceStrategy(base_path, "last_messages.txt");
                break;
        }

        RabbitMQChannel channel;
        String queue_type = p.getProperty("QUEUE_TYPE");
        switch (queue_type) {
            case "stream":
                int stream_size = Integer.parseInt(p.getProperty("MAXIMUM_STREAM_SIZE"));
                int segment_size = Integer.parseInt(p.getProperty("STREAM_SEGMENT_SIZE"));
                channel = new Stream(queue_name, stream_size, segment_size);
                break;

            case "quorum":
                channel = new QuorumQueues(queue_name);
                break;

            default:
                channel = new StandardQueue(queue_name);
                break;
        }

        String client_type = p.getProperty("CLIENT_TYPE");
        int DESIRED_PAYLOAD_IN_BYTE = Integer.parseInt(p.getProperty("DESIRED_PAYLOAD_IN_BYTE"));
        switch (client_type) {
            case "security_client":
                String KEY = p.getProperty("KEY");
                String ALGORITHM = p.getProperty("ALGORITHM");
                client = new SecurityClient(HOST, Integer.parseInt(PORT), USER, PASSWORD, KEY, ALGORITHM, DESIRED_PAYLOAD_IN_BYTE);
                break;

            default:
                client = new Client(HOST, Integer.parseInt(PORT), USER, PASSWORD, DESIRED_PAYLOAD_IN_BYTE);
                break;
        }
        client.setDataGenerator(dataGenerator);
        client.setPersistenceStrategy(persistenceStrategy);
        client.setChannel(channel);
        client.start();
    }

}
