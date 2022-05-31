package ConsumerDummy.Messages;

import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Hmac_Message_JsonMessage extends JsonMessage implements Hmac_Message {
    public static String HMAC_KEY = "HMAC";



    public Hmac_Message_JsonMessage(int sequence_number, String message, String algorithm, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        super(sequence_number, message);
        this.setHmac(this.calculateMac(algorithm,this,key));
    }

    public void setHmac(String hmac){
        JSONObject object = new JSONObject();
        if(this.json_message != null){
            object = new JSONObject(this.json_message);
        }
        object.put(HMAC_KEY,hmac);
        this.json_message = object.toString();
    }

    public String getHmac(){
        return new JSONObject(this.json_message).getString(HMAC_KEY);
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
