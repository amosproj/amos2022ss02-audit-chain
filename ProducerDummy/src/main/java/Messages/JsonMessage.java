package Messages;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class JsonMessage extends AbstractMessage {

    public JSONObject json_message;


    public JsonMessage(int sequence_number, String message) {
        super(sequence_number,message);
        this.formatMessage();
    }

    @Override
    public void formatMessage() {
        JSONObject json_message = new JSONObject();
        json_message.put("Sequence_Number",this.sequence_number);
        json_message.put("Messages.Message",this.message_string);
        this.json_message = json_message;
        return;
    }


    public JSONObject getMessage() {
        return this.json_message;
    }

    public byte[] serializeMessage(){
        return this.getMessage_string().toString().getBytes(StandardCharsets.UTF_8);
    }


}
