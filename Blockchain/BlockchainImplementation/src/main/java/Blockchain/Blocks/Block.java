package Blockchain.Blocks;

import Blockchain.Hashing.Hasher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Block implements BlockInterface {

    private final String previousHashBlock; /** contains the hash of the previous block */
    private String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    private Map<String, SubBlock> data;
    private int nonce; /** arbitrary number to be used in cryptography */

    public Block(String previousHashBlock, int prefix, File data) {
        this.previousHashBlock = previousHashBlock;
        extractDataFromFile(data);
        this.timestamp = new Date().getTime();
        this.hashBlock = calcHash();
        mineBlock(prefix);
    }

//    public Block(String previousHashBlock, int prefix, String JSON) {
//        this.previousHashBlock = previousHashBlock;
//        this.data = extractDataFromFile(data);
//        this.timestamp = new Date().getTime();
//        this.hashBlock = calcHash();
//        mineBlock(prefix);
//    }

    private String getDataHash () {
        String result = "";

        for (String key : data.keySet()) {
            result += key + data.get(key);
        }
        return Hasher.hashSHA256(result);
    }

    /**
     * Calculates the hash of the current block calling {@link Hasher#hashSHA256} method
     *
     * @return the hash of the current block
     * */
    private String calcHash() {
        return Hasher.hashSHA256(previousHashBlock, Long.toString(timestamp),
                Integer.toString(nonce), getDataHash());
    }

    /**
     * Mine the current block using a prefix with which the hash has to begin.
     * The nonce is incremented until the hash of the block begins with the prefix of 0's.
     *
     * @param prefix
     * @return the hash of the current block
     */
    private String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hashBlock.substring(0, prefix).equals(prefixString)) {
            nonce++;
            hashBlock = calcHash();
        }
        return hashBlock;
    }

    private void extractDataFromFile(File file)  {
        data = new HashMap<>();

       // BufferedReader reader = new BufferedReader(new FileReader(file));
    }

    /**
     * @return hash of the current block saved in the block
     */
    @Override
    public String getHashBlock () {
        return hashBlock;
    }

    @Override
    public String getPreviousHashBlock () { return previousHashBlock; }

    public Map<String, SubBlock> getData () { return data; }

    public long getTimestamp () { return timestamp; }



}
