package ProducerDummy.Messages;

import org.apache.commons.codec.digest.HmacUtils;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class Hmac_Message_SimpleMessage extends SimpleMessage implements Hmac_Message {

    private String hmac;

    public Hmac_Message_SimpleMessage(int sequence_number, String message, String algorithm, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        super(sequence_number, message);
        this.setHmac(this.calculateMac(algorithm,this,key));
    }


    private void setHmac(String hmac){
        this.hmac = hmac;
    }

    public String getHmac(){
        return this.hmac;
    }

    @Override
    public boolean verifyMAC(String algorithm, Message message, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        String calculated_hmac = this.calculateMac(algorithm,message,key);
        String message_hmac = this.getHmac();
        if(calculated_hmac.equals(message_hmac)){
            return true;
        }
        return false;
    }
@Override
    public String calculateMac(String algorithm,Message message ,String key) throws NoSuchAlgorithmException, InvalidKeyException {
        String data = message.toString();
        return new HmacUtils(algorithm,key).hmacHex(data);
    }

}
