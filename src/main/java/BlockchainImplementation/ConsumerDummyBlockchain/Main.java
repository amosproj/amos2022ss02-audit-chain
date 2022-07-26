package BlockchainImplementation.ConsumerDummyBlockchain;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import org.apache.commons.cli.*;

import ProducerDummy.ChannelSelection.QuorumQueues;
import ProducerDummy.ChannelSelection.StandardQueue;
import ProducerDummy.ChannelSelection.Stream;
import ProducerDummy.Client.AbstractClient;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {

        Options options = new Options();
        Option host = new Option( "h","host", true, "host ip");
        host.setRequired(false);
        options.addOption(host);
        Option port = new Option("p","port", true, "port");
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
            formatter.printHelp("java -jar target/AuditChain-Blockchain.jar --host 127.0.0.1 --port 5672 --username admin --password admin", options);
            System.exit(1);
        }

        String filepath = Paths.get("src","main","resources","BlockchainImplementation").toString();
        String filename = "config.properties";

        Path config_path = Paths.get(System.getProperty("user.dir"), filepath, filename);
        Properties p = new Properties();
        FileReader reader;
        try {
            reader = new FileReader(config_path.toString());
        } catch (IOException e) {
            //if we could not found it this folder we check the current one
            reader = new FileReader(Paths.get(System.getProperty("user.dir"), filename).toString());
        }
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
        String queue_name = p.getProperty("QUEUE_NAME");
        int gui_port = Integer.parseInt(p.getProperty("GUI_PORT"));
        String PATH = p.getProperty("PATH_BLOCKCHAIN_FILES");
        int MAX_BYTE = Integer.parseInt(p.getProperty("MAX_BYTE_PER_FILE"));
        String QUEUE_TYPE = p.getProperty("QUEUE_TYPE");
        AbstractClient client = new ConsumerClientBlockchain(HOST,Integer.parseInt(PORT),USER,PASSWORD, PATH, MAX_BYTE,gui_port);


        switch (QUEUE_TYPE) {
            case "standard":
                client.setChannel(new StandardQueue(queue_name));
                break;
            case "quorum":
                client.setChannel(new QuorumQueues(queue_name));
                break;
            case "stream":
                client.setChannel(new Stream(queue_name));
                break;
            default:
                throw new RuntimeException("No Valid Channel Value selected. standard,quorum or stream");
        }

        try {
            client.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return;
    }

}
