package Blockchain.Hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Hasher {

    /**
     * Hash a series of strings, concatenating them and then using SHA-256 (hash method used in cryptocurrencies)
     *
     * @param input The strings to be concatenated and then hashed
     * @return The hash of the concatenated string
     */
    public static String hashSHA256 (String ... input) {

        String plaintext = Arrays.stream(input)
                .sequential()
                .collect(Collectors.joining(""));

        MessageDigest digest = null;
        byte[] bytes = null;

        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(plaintext.getBytes(UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        StringBuilder buffer = new StringBuilder();

        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();

    }

}
