package ProducerDummy.Messages;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.HmacUtils;


public class Hmac_SimpleMessage extends SimpleMessage implements Hmac_Message {

    private String hmac;

    public Hmac_SimpleMessage(int sequence_number, String message, String algorithm, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        super(sequence_number, message);
        this.setHmac(this.calculateMac(algorithm,key));
    }

    public Hmac_SimpleMessage(int sequence_number, String message, String hmac)  {

        super(sequence_number, message);
        this.setHmac(hmac);

    }


    private void setHmac(String hmac){
        this.hmac = hmac;
    }

    public String getHmac(){
        return this.hmac;
    }

    @Override
    public boolean verifyMAC(String algorithm, String key) {

        String calculated_hmac = this.calculateMac(algorithm,key);

        if(calculated_hmac.equals(this.getHmac())){
            return true;
        }
        return false;

    }

    @Override
    public String calculateMac(String algorithm ,String key) {
        String data = this.toString();
        return new HmacUtils(algorithm,key).hmacHex(data);
    }

    @Override
    public int getPayloadSize() {
        return this.getMessage().getBytes(StandardCharsets.UTF_8).length + String.valueOf(this.getSequence_number()).getBytes(StandardCharsets.UTF_8).length + this.getHmac().getBytes(StandardCharsets.UTF_8).length;
    }

}