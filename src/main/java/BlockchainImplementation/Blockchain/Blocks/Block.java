package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;
import ProducerDummy.Messages.AggregateMessage;
import ProducerDummy.Messages.Message;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Block<T,R> {

    private final String previousHashBlock; /** contains the hash of the previous block */
    private String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    private Map<String, SubBlock> data;
    private int nonce; /** arbitrary number to be used in cryptography */
    private String lastSubBlockHash; /** contains the hash of the last sub block */

    public Block(String previousHashBlock, int prefix, T[] seq_number, R[] transactions) throws IOException {
        this.previousHashBlock = previousHashBlock;

        putData(seq_number, transactions);

        this.timestamp = new Date().getTime();
        this.hashBlock = calcHash();
        mineBlock(prefix);
    }


    private void putData(T[] seq_number, R[] transactions) {

        data = new HashMap<>();
        String previousHash = "0";

        for (int i = 0; i < seq_number.length; i++) {
            SubBlock newSubBlock = new SubBlock(previousHash, seq_number[i], transactions[i]);
            previousHash = newSubBlock.getHashBlock();

            data.put(previousHash, newSubBlock);
        }
    }

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

    /**
     * @return hash of the current block saved in the block
     */
    public String getHashBlock () {
        return hashBlock;
    }

    public String getPreviousHashBlock () { return previousHashBlock; }

    public Map<String, SubBlock> getData () { return data; }

    public long getTimestamp () { return timestamp; }


    public String toString() {

        String result = "[ ";

//        for (String key : data.keySet()) {
//            result += data.get(key).toString() + " ";
//        }

        return result + " ]";
    }
}
