package Messages;

public abstract class AbstractMessage {

    int sequence_number;
    String message_string;

    public AbstractMessage(int sequence_number, String message_string) {
        this.sequence_number = sequence_number;
        this.message_string = message_string;
    }


    public abstract void formatMessage();


    public abstract byte[] serializeMessage();

    public int getSequenceNumber(){
        return this.sequence_number;
    }

    public String getMessage_string(){
        return this.message_string;
    }


}
