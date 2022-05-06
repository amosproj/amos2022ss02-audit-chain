package Messages;

import java.nio.charset.StandardCharsets;

public class Message extends AbstractMessage {


    public Message(int sequence_number, String message) {
        super(sequence_number, message);
    }

    @Override
    public void formatMessage() {
        return;
    }

    @Override
    public byte[] serializeMessage() {
        return (Integer.toString(this.sequence_number) + this.message_string).getBytes(StandardCharsets.UTF_8);
    }
}
