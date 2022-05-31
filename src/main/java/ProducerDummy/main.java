package ProducerDummy;

import ProducerDummy.Client.AbstractClient;
import ProducerDummy.Client.AggregateClient;
import ProducerDummy.Messages.Hmac_Message;
import ProducerDummy.Messages.Hmac_Message_JsonMessage;
import ProducerDummy.Messages.Message;
import ProducerDummy.Messages.SimpleMessage;

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

        String hmacSHA256Algorithm = "HmacSHA256";
        String key = "0123456789";
        String key1 = "00000";


        Message m2 = new SimpleMessage(1,"dfd");
        Hmac_Message_JsonMessage m1 = new Hmac_Message_JsonMessage(1,"Hello_World",hmacSHA256Algorithm,key);

        boolean a = m1.verifyMAC(hmacSHA256Algorithm,key);
        boolean b = m1.verifyMAC(hmacSHA256Algorithm,key1);

        if(m1 instanceof Hmac_Message){
            //HMAC_Message
            int c = 5;
        }else{
            int c = 5;
        }







        AbstractClient client = new AggregateClient(HOST, PORT, USER, PASSWORD, queue_name);
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

