import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import org.json.JSONObject;

public class AggregateConsumerClient extends AbstractClient{

    static int sequence_number = 0;



    /**
     * Constructor for AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @throws IOException if the file cannot be read
     */
    public AggregateConsumerClient() throws IOException {
    }

    public void start() throws IOException, TimeoutException {
        FileWriter fw = new FileWriter(String.valueOf(Paths.get(System.getProperty("user.dir"), this.filepath, "Test.txt")), true);

        System.out.println("Starting to receive Messages.");
        Connection connection = this.factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println(" [*] Waiting for messages.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            JSONObject json = new JSONObject(new String(delivery.getBody(), StandardCharsets.UTF_8)); // transform Bytecode to a JsonObject
            HashMap<Integer, String> sorting = new HashMap<Integer, String>(); // Map the messages to (sequence number, message) values
            System.out.println(json.toString());
            for(String key : json.keySet()){
                sorting.put(Integer.parseInt(json.getJSONObject(key).get("Sequence_Number").toString()), json.getJSONObject(key).get("Message").toString()); //converting for the hashmap string into int for sequenznumber
            }

            System.out.println(sorting.toString()); // Testing the hashmap
            for(Object key : sorting.keySet().stream().sorted().toArray()){
                if(sequence_number == Integer.parseInt(key.toString())){ // comparing sequence number of message with expected number
                    fw.write(sequence_number + "  " + sorting.get(key) + '\n'); // if true write message to file
                    fw.flush();
                    System.out.println("Success! Adding message #" + key.toString() + " " +  sorting.get(key) + " to the database");
                    sequence_number++;
                }else{
                    System.out.println("Missing message, please contact your admin to solve that problem");
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
