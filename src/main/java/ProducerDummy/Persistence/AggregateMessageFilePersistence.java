package ProducerDummy.Persistence;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ProducerDummy.Messages.Hmac_JsonMessage;
import ProducerDummy.Messages.Hmac_SimpleMessage;
import ProducerDummy.Messages.JsonMessage;
import ProducerDummy.Messages.Message;
import ProducerDummy.Messages.SimpleMessage;


/***
 * This Implementation is Append only. It stores every Message it receives in a File.
 */

public class AggregateMessageFilePersistence extends FilePersistenceStrategy {

    /**
     * Constructor for the FilePersistenceStrategy.
     * Set filepath to the path of the file using path and fileName variables and
     * call {@link #CreatePersistenceMechanism()}.
     *
     * @param path
     * @param fileName
     * @throws IOException if the filepath is not valid
     */
    public AggregateMessageFilePersistence(String path, String fileName) throws IOException {
        super(path, fileName);
    }


    public void StoreMessage(Message message) {

        try {
            this.fileWriter = new FileWriter(filepath.toString(), true);
            gson.toJson(message.toSimpleFormat(), this.fileWriter);
            this.fileWriter.write(",");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }

    }

    @Override
    public ArrayList<Message> ReadLastMessage() {

        ArrayList<Message> messages = new ArrayList<>(100);

        try {
            // modify the file so gson can parse it
            JsonElement jsonElement = JsonParser.parseString("[" + Files.readString(getFilePath()).replaceAll(",$", "") + "]");
            if (jsonElement.isJsonArray()) {
                //more than one Objects
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                // last index is always zero, adjust index in loop
                for (int i = 1; i < jsonArray.size(); i++) {

                    JsonObject jsonObject = jsonArray.get(i - 1).getAsJsonObject();

                    String message_string = jsonObject.getAsJsonPrimitive(JsonMessage.MESSAGE_KEY).getAsString();
                    int sequence_number = jsonObject.getAsJsonPrimitive(JsonMessage.SEQUENCE_NUMBER).getAsInt();

                    // if there is a Hmac key we know it is a Hmac Message else it is just a normal Message
                    try {
                        String hmac = jsonObject.getAsJsonPrimitive(Hmac_JsonMessage.HMAC_KEY).getAsString();
                        messages.add(new Hmac_SimpleMessage(sequence_number, message_string, hmac));
                    } catch (NullPointerException e) {
                        messages.add(new SimpleMessage(sequence_number, message_string));
                    }

                }
                return messages;
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }

        return null;
    }

}