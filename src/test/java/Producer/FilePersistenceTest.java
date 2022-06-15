package Producer;


import ProducerDummy.Messages.*;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;
import ProducerDummy.Persistence.FilePersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;


import com.google.gson.*;
import org.json.JSONArray;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.junit.Assert.*;

public class FilePersistenceTest {
String current_path = System.getProperty("user.dir");
String filepath;
String filename;
String HOST;
int PORT;
String USER;
String PASSWORD;
String queue_name;

    @Before
    public void setUp() throws IOException {
        filepath = Paths.get(current_path,"src", "main", "resources", "ProducerDummy").toString();
        filename = "testfile.txt";

        Path config_path = Paths.get(filepath, "config.properties");
        Properties p = new Properties();
        FileReader reader = new FileReader(config_path.toString());
        p.load(reader);

        HOST = p.getProperty("HOST");
        PORT = Integer.parseInt(p.getProperty("PORT"));
        USER = p.getProperty("USERNAME");
        PASSWORD = p.getProperty("PASSWORD");
        queue_name = "FAKE";
    }



    @Test
    public void CreateFile() throws IOException {
        assertFalse(Files.exists(Paths.get(filepath, filename)));
        PersistenceStrategy filePersistenceStrategy = new FilePersistenceStrategy(filepath, filename);
        assertTrue(Files.exists(Paths.get(filepath, filename)));
    }

    @Test
    public void FileAlreadyExists() throws IOException {
        PersistenceStrategy filePersistenceStrategy = new FilePersistenceStrategy(filepath, filename);
        assertTrue(Files.exists(Paths.get(filepath, filename)));
        filePersistenceStrategy = new FilePersistenceStrategy(filepath, filename);
        assertTrue(Files.exists(Paths.get(filepath, filename)));
    }

@Before @After
    public void DeleteFile() {
        String s = Paths.get(filepath, filename).toString();
        File file = new File(Paths.get(filepath, filename).toString());
        file.delete();
    }

    @Test
    public void StoreAndReadMessageTest() throws IOException {
        PersistenceStrategy filePersistenceStrategy = new FilePersistenceStrategy(filepath ,filename);
        Message message = new SimpleMessage(0, "Hello World");
        filePersistenceStrategy.StoreMessage(message);
        Message message1 = filePersistenceStrategy.ReadLastMessage();
        assertEquals(message.getMessage(), message1.getMessage());
        assertEquals(message.getSequence_number(), message1.getSequence_number());
    }


    @Test
    public void StoreAndReadAggregateMessageTest() throws IOException {
        AggregateMessageFilePersistence filePersistenceStrategy = new AggregateMessageFilePersistence(filepath, filename);
        Message message = new SimpleMessage(0, "Hello World");
        Message message1 = new SimpleMessage(1, "New Hello World");
        filePersistenceStrategy.StoreMessage(message);
        filePersistenceStrategy.StoreMessage(message1);
        filePersistenceStrategy.StoreMessage(message1);


        AggregateMessage message2 = (AggregateMessage) filePersistenceStrategy.ReadLastMessage();
        Vector<Message> messages = message2.getMessages();
        Message m = messages.get(0);
        Message m1 = messages.get(1);

        assertEquals(message.getMessage(), m.getMessage());
        assertEquals(message.getSequence_number(), m.getSequence_number());
        assertEquals(message1.getMessage(), m1.getMessage());
        assertEquals(message1.getSequence_number(), m1.getSequence_number());
    }


    @Test(expected = NullPointerException.class)
    public void StoreNullMessageTest() throws IOException {
        AggregateMessageFilePersistence filePersistenceStrategy = new AggregateMessageFilePersistence(filepath, filename);
        Message message = null;
        filePersistenceStrategy.StoreMessage(message);

    }


        @Test
    public void test() throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        String KEY = "0123456";
        String ALGORITHM = "HmacSHA256";
        String s = Paths.get(filepath, filename).toString();


        Gson gson = new Gson();


        Vector<Message> m = new Vector<>();
        Message message = new Hmac_SimpleMessage(1, "Hello World", ALGORITHM, KEY);
        m.add(message);
        Message message2 = new SimpleMessage(1, "fsdfds");
        m.add(message2);
        JSONArray jsonArray = new JSONArray();
        jsonArray.putAll(m);

        FileWriter fw = new FileWriter(s);

        gson.toJson(m,fw);
        fw.close();

        return;




    }



    @Test
    public void test2() throws IOException, ClassNotFoundException {
        String s = Paths.get(filepath, filename).toString();

        String HMAC = "hmac";
        String SEQUENCE_NUMER = "sequence_number";
        String MESSAGE_STRING = "message";


        File file = new File(s);
        Gson gson = new Gson();
        // Parse whole file
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(file));
        //convert to json array
        JsonArray m = jsonElement.getAsJsonArray();
        for(int i=0;i<m.size();i++){
            Message m1;
            JsonObject o1 =  m.get(i).getAsJsonObject();
            int sequence_number = Integer.parseInt(o1.getAsJsonPrimitive(SEQUENCE_NUMER).toString());
            String message_string = o1.getAsJsonPrimitive(MESSAGE_STRING).toString();
            try{
                String hmac = o1.getAsJsonPrimitive(HMAC).toString();
                m1 = new Hmac_JsonMessage(sequence_number,message_string,hmac);
                m1 = (Hmac_Message) m1;

            }catch (NullPointerException e){
                m1 = new JsonMessage(sequence_number,message_string);

            }
        }


    }


}

