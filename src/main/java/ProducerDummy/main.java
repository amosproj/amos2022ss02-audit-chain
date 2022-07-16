package ProducerDummy;

import java.io.File;
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

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {

        Options options = new Options();
        Option host = new Option( "host", false, "host ip");
        host.setRequired(false);
        options.addOption(host);
        Option port = new Option("port", false, "port");
        port.setRequired(false);
        options.addOption(port);
        Option username = new Option("username", true, "username for the broker(rabbitmq)");
        username.setRequired(false);
        options.addOption(username);
        Option password = new Option("password", true, "password for the broker(rabbitmq)");
        password.setRequired(false);
        options.addOption(password);

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("ProducerDummy -host 127.0.0.1 -port 5672 -username admin -password admin", options);
            System.exit(1);
        }

        String HOST, PORT, USER, PASSWORD;

        HOST = cmd.getOptionValue("host");
        PORT = cmd.getOptionValue("port");
        USER = cmd.getOptionValue("username");
        PASSWORD = cmd.getOptionValue("password");

        Path path = Paths.get(System.getProperty("user.dir"));
        String filepath = Paths.get("src", "main", "resources","ProducerDummy").toString();
        String filename = "config.properties";
        Path config_path = Paths.get(System.getProperty("user.dir"), filepath, filename);

        File file = new File(String.valueOf(config_path));
        if(file.exists() && !file.isDirectory()) {
            FileReader reader;
            try{
                reader = new FileReader(config_path.toString());
            }catch (IOException e){
                //if we could not found it this folder we check the current one
                reader = new FileReader(Paths.get(System.getProperty("user.dir"),filename).toString());
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

            // create components for Client
            DataGenerator dataGenerator;
            String data_generator_type = p.getProperty("DATA-GENERATOR_TYPE");
            switch (data_generator_type) {
                case "filedatereader":
                    dataGenerator = new FileDataReader(base_path, "household_power_consumption.txt");
                    break;

                default:
                    dataGenerator = new DynamicDataGenerator();
                    break;
            }

            PersistenceStrategy filePersistenceStrategy;
            String persistence_strategy_type = p.getProperty("PERSISTENCE-STRATEGY_TYPE");

            switch (persistence_strategy_type) {
                case "aggregate-message":
                    filePersistenceStrategy = new AggregateMessageFilePersistence(base_path, "last_messages.txt");
                    break;

                case "file":
                    filePersistenceStrategy = new FilePersistenceStrategy(base_path, "last_messages.txt");
                    break;

                default:
                    filePersistenceStrategy = new NullObjectPersistenceStrategy(base_path, "last_messages.txt");
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


            Producer client;
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
            client.setPersistenceStrategy(filePersistenceStrategy);
            client.setChannel(channel);
            client.start();

            return;
        }
        else{
            System.out.println("config.properties not found at " + filepath );
        }
    }

}