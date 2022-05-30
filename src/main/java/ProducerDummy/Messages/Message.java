package ProducerDummy.Messages;

import java.io.Serializable;

public interface Message extends Serializable {

    public Message getMessageObject();
    public void formatMessage(int sequence_number, String message);

    public int getSequence_number();
    public void setSequence_number(int sequence_number);

    public String getMessage();
    public void setMessage(String json_message);



}