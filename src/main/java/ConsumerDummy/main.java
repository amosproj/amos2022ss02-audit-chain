package ConsumerDummy;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import ConsumerDummy.Client.AggregateConsumerClient;

public class main {

    public static void main(String[] args) throws IOException, TimeoutException {

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
        String queue_name = "FAKE";

        AggregateConsumerClient client = new AggregateConsumerClient(HOST,PORT,USER,PASSWORD,queue_name);
        client.start();
        return;
    }

}