package Messages;

/**
 * Abstract class for all type of Messages.
 */
public abstract class AbstractMessage {

    int sequence_number;
    String message_string;

    /**
     * Constructor for AbstractMessage.
     *
     * @param sequence_number sequence number of the messages. Used to keep the cardinality of the messages in the order
     *                       they are sent from the client
     * @param message_string message as a string
     */
    public AbstractMessage(int sequence_number, String message_string) {
        this.sequence_number = sequence_number;
        this.message_string = message_string;
    }

    /**
     * Prepare the message packet depending on the implementation of the Message.
     */
    public abstract void formatMessage();

    /**
     * @return the message as a byte array
     */
    public abstract byte[] serializeMessage();

    /**
     * @return sequence number of the number
     */
    public int getSequenceNumber(){
        return this.sequence_number;
    }

    /**
     * @return the message as a string
     */
    public String getMessage_string(){
        return this.message_string;
    }


}
