import Messages.Hmac_JsonMessage;
import Messages.Hmac_SimpleMessage;
import Messages.JsonMessage;

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


        Hmac_JsonMessage message = new Hmac_JsonMessage(1,"HelloWorld",hmacSHA256Algorithm,key);
        Hmac_JsonMessage message2 = new Hmac_JsonMessage(1,"HelloWorl",hmacSHA256Algorithm,key);

        if(message.verifyMAC(hmacSHA256Algorithm,message,key)){
            System.out.println("Hash is the Same!");
        }
        if(message.verifyMAC(hmacSHA256Algorithm,message2,key)){
            System.out.println("Hash is the Same!");
        }else{
            System.out.println("Hash is not the Same!");
        }





        //Client client = new Client(HOST,PORT,USER,PASSWORD,queue_name);
        //client.start();




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
