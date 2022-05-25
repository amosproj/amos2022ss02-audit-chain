import Messages.*;
import Persistence.AggregateMessageFilePersistence;
import Persistence.FilePersistenceStrategy;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class main {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {
        String filepath = "\\ProducerDummy\\src\\main";
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

        String hmacSHA256Algorithm = "HmacSHA256";
        String key = "0123456789";
        String path = "\\ProducerDummy\\src\\main\\";


        FilePersistenceStrategy filePersistenceStrategy = new AggregateMessageFilePersistence(path,"messages.txt");




        AbstractClient client = new Client(HOST,PORT,USER,PASSWORD,queue_name);

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
