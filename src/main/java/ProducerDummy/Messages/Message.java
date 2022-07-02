package ProducerDummy.Messages;

import java.io.Serializable;


/**
 * Implementation of a normal Message which only has a Sequence Nr (event number) and a String
 */
public interface Message extends Serializable {

    public int getSequence_number();

    public void setSequence_number(int sequence_number);

    public String getMessage();

    public void setMessage(String json_message);

    // the SimpleFormat is how Messages are stored external
    public Message toSimpleFormat();

}