package ProducerDummy.Messages;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface Hmac_Message extends Message {


    public String getHmac();

    public boolean verifyMAC(String algorithm, String key);
    public String calculateMac(String algorithm, String key) throws NoSuchAlgorithmException, InvalidKeyException;



}