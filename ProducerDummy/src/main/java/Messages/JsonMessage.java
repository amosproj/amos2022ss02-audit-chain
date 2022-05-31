package Messages;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/** Message of type JSON */
public class JsonMessage extends AbstractMessage {

    public JSONObject json_message;

    /**
     * Constructor for JsonMessage.
     * call {@link #formatMessage()} to format the message
     *
     * @param sequence_number sequence number of the messages. Used to keep the cardinality of the messages in the order
     *                        they are sent from the client
     * @param message message as a string
     */
    public JsonMessage(int sequence_number, String message) {
        super(sequence_number,message);
        this.formatMessage();
    }

    public JsonMessage() {
        super();
    }


    /**
     * {@inheritDoc}
     * As a JsonMessage, the message is formatted as a JSONObject
     */
    @Override
    public void formatMessage() {
        JSONObject json_message = new JSONObject();
        json_message.put("Sequence_Number",this.sequence_number);

        json_message.put("Messages.Message",this.message_string);
        this.json_message = json_message;
        return;
    }

    /**
     * @return the JSONObject of the message
     */
    public JSONObject getMessage() {
        return this.json_message;
    }


    @Override
    public byte[] serializeMessage(){
        return this.getMessage_string().toString().getBytes(StandardCharsets.UTF_8);
    }


}
