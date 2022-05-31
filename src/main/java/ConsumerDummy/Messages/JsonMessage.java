package ConsumerDummy.Messages;

import org.json.JSONObject;

public class JsonMessage implements Message {
    String json_message;
    public static String MESSAGE_KEY = "Message";
    public static String SEQUENCE_KEY = "Sequence_Number";
    public JsonMessage(int sequence_number, String json_message) {
        this.formatMessage(sequence_number, json_message);
    }



    @Override
    public void formatMessage(int sequence_number, String message) {
        this.setMessage(message);
        this.setSequence_number(sequence_number);
        return;
    }

    @Override
    public int getSequence_number() {
        return new JSONObject(this.json_message).getInt(SEQUENCE_KEY);
    }

    @Override
    public void setSequence_number(int sequence_number) {
        JSONObject object = new JSONObject();
        if(this.json_message != null) {
            object = new JSONObject(this.json_message);
        }
        object.put(SEQUENCE_KEY,sequence_number);
        this.json_message = object.toString();

    }

    @Override
    public String getMessage() {
        return new JSONObject(this.json_message).getString(MESSAGE_KEY);
    }

    @Override
    public void setMessage(String json_message) {
        JSONObject object = new JSONObject();
        if(this.json_message != null){
             object = new JSONObject(this.json_message);
        }
        object.put(MESSAGE_KEY, json_message);
        this.json_message = object.toString();

    }

    @Override
    public Message getMessageObject() {
        return (JsonMessage) this;
    }

    public SimpleMessage toSimpleMessage(){
        return new SimpleMessage(this.getSequence_number(),this.getMessage());
    }
    public String toString(){
        return Integer.toString(this.getSequence_number()) + this.getMessage();
    }





}
