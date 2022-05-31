package Client;

import Messages.AggregateMessage;
import Messages.JsonMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class AggregateConsumerClientTest {

    @Test
    @DisplayName("Testing localhost functionality")
    void localhost() {
        try {
            AggregateConsumerClient c = new AggregateConsumerClient("localhost", 5672, "shouldn't be", "set in factory", "Fake");
            assertAll(
                    () -> assertEquals("localhost", c.factory.getHost()),
                    () -> assertEquals(5672, c.factory.getPort()),
                    () -> assertEquals("guest", c.factory.getUsername()),  //should be guest in localhost
                    () -> assertEquals("guest", c.factory.getPassword())); //should be guest in localhost
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test basic functions")
    void start() throws IOException, TimeoutException {
        String path = Paths.get(System.getProperty("user.dir"), "src", "main", "messages.txt").toString();
        //Deleting file if it already exists
        File f = new File(path);
        f.delete();

        //Testing of messaging.txt is created
        AggregateConsumerClient c = new AggregateConsumerClient("localhost", 5672, " ", " ", "Fake");
        assertTrue(f.exists());

        // Maybe implement a RabbitMQ mock queue (but is this really necessary?)



    }

    @Test
    @DisplayName("Test deserialize message")
    void deserializeMessage() throws IOException, ClassNotFoundException {

        AggregateMessage m = new AggregateMessage(); // generate AggregateMessage
        for(int i = 0; i < 5; i++){
            m.addMessage(new JsonMessage(i, "Test " + i));  // adding 5 messages
        }

        byte[] bytecode = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // 64 - 71 serialize object
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(m);
            bytecode = out.toByteArray();
        } catch (IOException e) {
        }
        assertNotNull(bytecode); // check if serialization worked

        AggregateMessage mDeserialized = (AggregateMessage) AggregateConsumerClient.deserializeMessage(bytecode); //deserialization
        Vector messages  = mDeserialized.getMessages(); //get messages

        for(int i = 0; i < 5; i++){
            assertEquals(i , ((JsonMessage) messages.get(i)).getSequence_number()); //check if sequence number is correct
            assertEquals("Test " + i, ((JsonMessage) messages.get(i)).getMessage()); //check if message is correct
        }



    }
}