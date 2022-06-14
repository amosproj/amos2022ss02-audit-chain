package ProducerDummy;

import ProducerDummy.Client.AbstractClient;
import ProducerDummy.Client.AggregateClient;
import ProducerDummy.Client.Client;
import ProducerDummy.Client.Producer;
import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.DataGeneration.FileDataReader;
import ProducerDummy.Messages.*;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;
import ProducerDummy.Persistence.FilePersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class main {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {
        String filepath = Paths.get("src", "main", "java","ProducerDummy").toString();
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



        String base_path = Paths.get(System.getProperty("user.dir"),filepath).toString();
        DataGenerator dataGenerator = new FileDataReader(base_path,"household_power_consumption.txt");
        PersistenceStrategy filePersistenceStrategy = new AggregateMessageFilePersistence(base_path,"last_messages.txt");


        Producer client = new AggregateClient(HOST, PORT, USER, PASSWORD, queue_name);
        client.setDataGenerator(dataGenerator);
        client.setPersistenceStrategy(filePersistenceStrategy);
        client.start();




        return;
    }

    public static byte[] serialize(Object object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(object);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}

