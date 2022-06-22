package ProducerDummy.Persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ProducerDummy.Messages.Hmac_JsonMessage;
import ProducerDummy.Messages.Hmac_SimpleMessage;
import ProducerDummy.Messages.JsonMessage;
import ProducerDummy.Messages.Message;
import ProducerDummy.Messages.SimpleMessage;

/***
 * This is one Implementation which is supposed to guarantee us that we never lose our current state of the Sequence Number.
 */

public class FilePersistenceStrategy implements PersistenceStrategy {

    protected Path filepath;
    protected FileWriter fileWriter;
    protected Gson gson = new Gson();

    /**
     * Constructor for the FilePersistenceStrategy.
     * Set filepath to the path of the file using path and fileName variables and
     * call {@link #CreatePersistenceMechanism()}.
     *
     * @throws IOException if the filepath is not valid
     */
    public FilePersistenceStrategy(String path, String fileName) throws IOException {
        this.filepath = Paths.get(path, fileName);
        this.CreatePersistenceMechanism();
    }

    /***
     * Store just the last message into a file, overriding the previous one.
     *
     * @param message message to be stored as a string
     */
    @Override
    public void StoreMessage(Message message) throws NullPointerException {

        try {
            // Open FileWrite and overwrite last Messages.Message
            this.fileWriter = new FileWriter(filepath.toString());
            gson.toJson(message.toSimpleFormat(), fileWriter);
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (NullPointerException e){
            throw new NullPointerException();
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
                System.out.println(String.format("File %s already exists", file.getName()));
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

            //this should be the only valid state, since in the single Filemode only the last Message will be stored
            if (jsonElement.isJsonObject()) {

                // every Message consists of at least sequence_number and message_string
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String message_string = jsonObject.getAsJsonPrimitive(JsonMessage.MESSAGE_KEY).getAsString();
                int sequence_number = jsonObject.getAsJsonPrimitive(JsonMessage.SEQUENCE_NUMBER).getAsInt();

                // if there is a Hmac key we know it is a Hmac Message else it is just a normal Message
                try {
                    String hmac = jsonObject.getAsJsonPrimitive(Hmac_JsonMessage.HMAC_KEY).getAsString();
                    return new Hmac_SimpleMessage(sequence_number, message_string, hmac);
                } catch (NullPointerException e) {
                    return new SimpleMessage(sequence_number, message_string);
                }

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            //thats means there is no message inside
            return null;
        }

        return null;
    }

    @Override
    public Path getFilePath() {
        return this.filepath;
    }

    @Override
    public void cleanFile() {

        try {
            this.fileWriter.close();
        } catch (IOException e) {
            System.out.println("Already Closed");
        }catch (NullPointerException e){
            //happens iw storeMessage was never called --> File is closed
        }

        try {
            PrintWriter pw = new PrintWriter(this.getFilePath().toString());
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found, could not delete" + this.getFilePath().toString());
            throw new RuntimeException(e);
        }

    }

}