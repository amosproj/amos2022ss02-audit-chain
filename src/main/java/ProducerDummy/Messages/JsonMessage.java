package ProducerDummy.Messages;

import com.google.gson.JsonObject;

public class JsonMessage implements Message {

    JsonObject json_message = new JsonObject();
    public static String MESSAGE_KEY = "message";
    public static String SEQUENCE_NUMBER = "sequence_number";

    public JsonMessage(int sequence_number, String json_message) {
        this.formatMessage(sequence_number, json_message);
    }


    @Override
    public void formatMessage(int sequence_number, String message) {
        this.setMessage(message);
        this.setSequence_number(sequence_number);
    }

    @Override
    public int getSequence_number() {
        return this.json_message.get(SEQUENCE_NUMBER).getAsInt();
    }

    @Override
    public void setSequence_number(int sequence_number) {
        this.json_message.addProperty(SEQUENCE_NUMBER, sequence_number);
    }

    @Override
    public String getMessage() {
        return this.json_message.get(MESSAGE_KEY).getAsString();
    }

    @Override
    public void setMessage(String message) {
        this.json_message.addProperty(MESSAGE_KEY, message);

    }

    @Override
    public Message toSimpleFormat() {
        return new SimpleMessage(this.getSequence_number(),this.getMessage());
    }


    public String toString() {
        return this.getSequence_number() + this.getMessage();
    }


}