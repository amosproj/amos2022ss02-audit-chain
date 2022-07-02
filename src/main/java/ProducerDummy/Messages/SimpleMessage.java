package ProducerDummy.Messages;

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