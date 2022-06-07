package main.Test;



import ProducerDummy.Messages.*;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;
import ProducerDummy.Persistence.FilePersistenceStrategy;
import ProducerDummy.Persistence.PersistenceStrategy;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

public class FilePersistenceTest {

    String current_path = System.getProperty("user.dir");
    String path = "";
    String filename = "testfile.txt";




    @Test
    public void CreateFile() throws IOException {
        assertFalse(Files.exists(Paths.get(current_path,path,filename)));
        PersistenceStrategy filePersistenceStrategy = new FilePersistenceStrategy(path,filename);
        assertTrue(Files.exists(Paths.get(current_path,path,filename)));


    }

    @Before
    public void DeleteFile(){
        String s = Paths.get(current_path,path,filename).toString();
        File file = new File(Paths.get(current_path,path,filename).toString());
        file.delete();
    }

    @Test
    public void StoreAndReadMessageTest() throws IOException {
        PersistenceStrategy filePersistenceStrategy = new FilePersistenceStrategy(path,filename);
        Message message = new SimpleMessage(0,"Hello World");
        filePersistenceStrategy.StoreMessage(message);
        Message message1 = filePersistenceStrategy.ReadLastMessage();
        assertEquals(message.getMessage(),message1.getMessage());
        assertEquals(message.getSequence_number(),message1.getSequence_number());
    }


    @Test
    public void StoreAndReadAggregateMessageTest() throws IOException {
        AggregateMessageFilePersistence filePersistenceStrategy = new AggregateMessageFilePersistence(path,filename);
        Message message = new SimpleMessage(0,"Hello World");
        Message message1 = new SimpleMessage(1,"New Hello World");
        filePersistenceStrategy.StoreMessage(message);
        filePersistenceStrategy.StoreMessage(message1);

        AggregateMessage message2 = (AggregateMessage) filePersistenceStrategy.ReadLastMessage();
        Vector<Message> messages = message2.getMessages();
        Message m = messages.get(0);
        Message m1 = messages.get(1);

        assertEquals(message.getMessage(),m.getMessage());
        assertEquals(message.getSequence_number(),m.getSequence_number());
        assertEquals(message1.getMessage(),m1.getMessage());
        assertEquals(message1.getSequence_number(),m1.getSequence_number());


    }







}
