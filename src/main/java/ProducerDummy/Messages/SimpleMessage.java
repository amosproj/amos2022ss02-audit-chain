package ProducerDummy.Messages;

import ProducerDummy.ChannelSelection.Stream;

import java.nio.charset.StandardCharsets;

/**
 * Implementation of the Message interface in it´s most simple Form
 */
public class SimpleMessage implements Message {

    public int getSequence_number() {
        return sequence_number;
    }

    public void setSequence_number(int sequence_number) {
        this.sequence_number = sequence_number;
    }

    public void setMessage(String json_message) {
        this.message = json_message;
    }

    @Override
    public Message toSimpleFormat() {
        return this;
    }

    @Override
    public int getPayloadSize() {
        return this.getMessage().getBytes(StandardCharsets.UTF_8).length + String.valueOf(this.getSequence_number()).getBytes(StandardCharsets.UTF_8).length;
    }


    public String getMessage() {
        return this.message;
    }

    private int sequence_number;
    private String message;

    public SimpleMessage(int sequence_number, String message) {
        this.sequence_number = sequence_number;
        this.message = message;
    }

    public String toString(){
        return Integer.toString(this.getSequence_number())+this.getMessage();
    }

}