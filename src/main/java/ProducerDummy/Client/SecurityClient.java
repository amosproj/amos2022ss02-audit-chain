package ProducerDummy.Client;

import ProducerDummy.Messages.Hmac_JsonMessage;
import ProducerDummy.Messages.Hmac_Message;
import ProducerDummy.Messages.Hmac_SimpleMessage;
import ProducerDummy.Messages.Message;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SecurityClient extends Client{
    String key = null;
    String algorithm = null;


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