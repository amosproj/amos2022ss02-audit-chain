package Messages;

import java.util.Vector;

public class AggregateMessage implements Message {

    Vector<Message> messages = new Vector<>();

    public AggregateMessage() {

    }

    public void addMessage(Message message){
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
        return messages.get(messages.size()-1).getSequence_number();
    }

    @Override
    public void setSequence_number(int sequence_number) {
        return;
    }

    @Override
    public String getMessage() {
        return messages.get(messages.size()-1).getMessage();
    }

    @Override
    public void setMessage(String json_message) {
        return;
    }
}
