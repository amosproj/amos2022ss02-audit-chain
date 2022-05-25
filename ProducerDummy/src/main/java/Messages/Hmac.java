package Messages;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface Hmac {



    public boolean verifyMAC(String algorithm, Message message, String key) throws NoSuchAlgorithmException, InvalidKeyException;
    public String calculateMac(String algorithm,Message message, String key) throws NoSuchAlgorithmException, InvalidKeyException;



}
