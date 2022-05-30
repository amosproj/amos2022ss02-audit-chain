package ProducerDummy.Messages;

import java.util.Vector;

public class AggregateMessage implements Message {


    Vector<Message> messages = new Vector<>();


    public AggregateMessage() {

    }

    public AggregateMessage(int sequence_number, String message) {
        this.addMessage(new JsonMessage(sequence_number, message));
    }

    public AggregateMessage(int sequence_number) {
        this.addMessage(new JsonMessage(sequence_number, ""));
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }


    @Override
    public Message getMessageObject() {
        return this;
    }

    @Override
    public void formatMessage(int sequence_number, String message) {
        return;
    }

    @Override
    public int getSequence_number() {
        return this.messages.get(messages.size() - 1).getSequence_number();
    }

    @Override
    public void setSequence_number(int sequence_number) {
        this.messages.get(this.messages.size() - 1).setSequence_number(sequence_number);
        return;
    }

    @Override
    public String getMessage() {
        return messages.get(messages.size() - 1).getMessage();
    }

    @Override
    public void setMessage(String message) {
        this.messages.get(this.messages.size() - 1).setMessage("test");
        return;
    }

    public int getMessageSize() {
        return messages.size();
    }

    public Vector<Message> getMessages(){
        return this.messages;
    }

}