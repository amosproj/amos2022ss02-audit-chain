package ProducerDummy.Client;
import ProducerDummy.Messages.Hmac_SimpleMessage;
import ProducerDummy.Messages.Message;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * Extension of a Client which extends a Message with a MAC in order to detect tempered Messages/Events
 */
public class SecurityClient extends Client{
    private String key = null;
    private String algorithm = null;

    public SecurityClient(String host, int port, String username, String password,String key, String algorithm, int desired_payload_in_byte)throws IOException {
        super(host, port, username, password,desired_payload_in_byte);
        this.key = key;
        this.algorithm = algorithm;
    }
    public SecurityClient(String host, int port, String username, String password,String key, String algorithm) throws IOException {
        super(host, port, username, password);
        this.key = key;
        this.algorithm = algorithm;
    }


    @Override
    protected Message createMessage(int sequence_number,String message_string) {
        try {
            return new Hmac_SimpleMessage(sequence_number,message_string,this.algorithm,this.key);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }



}
