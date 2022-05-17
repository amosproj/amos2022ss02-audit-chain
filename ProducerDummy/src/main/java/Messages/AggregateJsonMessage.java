package Messages;

import org.json.JSONObject;

import java.util.Vector;

public class AggregateJsonMessage extends JsonMessage {


    Vector<Message> messages = new Vector<Message>();



    /**
     * Constructor for JsonMessage.
     * call {@link #formatMessage()} to format the message
     *
     * @param sequence_number sequence number of the messages. Used to keep the cardinality of the messages in the order
     *                        they are sent from the client
     * @param message         message as a string
     */
    public AggregateJsonMessage(int sequence_number, String message) {
        super(sequence_number, message);
    }

    public AggregateJsonMessage() {
        super();

    }

    public void addMessage(int sequence_numer,String message){
        messages.add(new Message(sequence_numer,message));
    }

    /**
     * {@inheritDoc}
     * As a JsonMessage, the message is formatted as a JSONObject
     */
    @Override
    public void formatMessage() {
        JSONObject json_message = new JSONObject();

        for(int i=0;i<messages.size();i++){
            JSONObject message = new JSONObject();
            message.put("Sequence_Number",messages.elementAt(i).sequence_number);
            message.put("Message",messages.elementAt(i).message_string);
            json_message.put("Message "+Integer.toString(i),message);
        }
        this.json_message = json_message;
        this.message_string =this.json_message.toString();
        return;
    }



}
