//TODO Refactor Name to Client and Client to DummyClient

import DataGeneration.DataGenerator;
import Persistence.PersistenceStrategy;
import com.rabbitmq.client.ConnectionFactory;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AbstractClient {

    protected DataGenerator dataGenerator;
    protected PersistenceStrategy persistenceStrategy;
    protected Path config_path;
    protected String filename = "config.properties";
    protected String filepath = "\\ProducerDummy\\src\\main";
    protected final static String QUEUE_NAME = "FAKE";
    protected String HOST;
    protected int PORT;
    protected String USER;
    protected String PASSWORD;
    protected ConnectionFactory factory = new ConnectionFactory();

    public AbstractClient() throws IOException {
        this.config_path = Paths.get(System.getProperty("user.dir"), this.filepath, this.filename);
        Properties p = new Properties();
        String a = this.config_path.toString();
        FileReader reader = new FileReader(this.config_path.toString());
        p.load(reader);
        this.HOST = p.getProperty("HOST");
        this.PORT = Integer.parseInt(p.getProperty("PORT"));
        this.USER = p.getProperty("USERNAME");
        this.PASSWORD = p.getProperty("PASSWORD");
        this.initFactory();
    }


    public void initFactory() {
        this.factory.setHost(this.HOST);
        this.factory.setUsername(this.USER);
        this.factory.setPassword(this.PASSWORD);
        this.factory.setPort(this.PORT);
    }


}