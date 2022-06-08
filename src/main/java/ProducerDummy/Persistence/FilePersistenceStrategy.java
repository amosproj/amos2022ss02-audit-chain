package ProducerDummy.Persistence;

import ProducerDummy.Messages.JsonMessage;
import ProducerDummy.Messages.Message;
import com.google.gson.Gson;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;



import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


/***
 * This is one Implementation which is supposed to guarantee us that we never lose our current state of the Sequence Number.
 */

public class FilePersistenceStrategy implements PersistenceStrategy {

    protected Path filepath;
    protected FileWriter fileWriter;
    private Gson gson = new Gson();

    /**
     * Constructor for the FilePersistenceStrategy.
     * Set filepath to the path of the file using path and fileName variables and
     * call {@link #CreatePersistenceMechanism()}.
     *
     * @throws IOException if the filepath is not valid
     */
    public FilePersistenceStrategy(String path,String fileName) throws IOException {
        this.filepath = Paths.get(path, fileName);
        this.CreatePersistenceMechanism();
    }

    /***
     * Store just the last message into a file, overriding the previous one.
     *
     * @param message message to be stored as a string
     */
    @Override
    public void StoreMessage(Message message) {
        try {
            // Open FileWrite and overwrite last Messages.Message
            this.fileWriter = new FileWriter(filepath.toString());
            gson.toJson(message.toString(),fileWriter);
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Create a File as a persistence mechanism.
     */
    @Override
    public void CreatePersistenceMechanism() {
        // Create the File for the last Messages.Message
        File file = new File(this.filepath.toString());

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println(String.format("File %s already exists",file.getName()));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Read from the file the last sequence number and the last message, returning them as a new Message object. If there are no
     * values in the file or the file is missing it means that we never did send a Messages.Message to the Broker
     *
     * @return a new Message object with the sequence number and message
     */
    @Override
    public Message ReadLastMessage() {

        File file = new File(this.filepath.toString());

        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(file));
            JsonArray m = jsonElement.getAsJsonArray();



        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }catch (IllegalStateException e){
            //thats means there is no message inside
            return null;
        }

        return null;
    }

    /**
     * @return filepath as string
     */
    public String getFilePath() {
        return this.filepath.toString();
    }


}