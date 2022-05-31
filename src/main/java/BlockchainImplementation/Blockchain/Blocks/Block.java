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

/**
 * A Block is the unit of the blockchain. It contains the pointer to its previous block, a hashmap of data in which the
 * SubBlocks are stored, and other information. It has to be mined in order to be added to the blockchain. Which means
 * that a hash puzzle is solved in order to find the correct hash.
 *
 * @param <T> The type of the meta_data (it could be everything, e.g. sequence number) of the information contained
 *  *           in the SubBlock.
 * @param <R> The type of the information contained in the SubBlock.
 */
public class Block<T,R> {

    private final String previousHashBlock; /** contains the hash of the previous block */
    private String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    private Map<String, SubBlock> data; /** contains the data (composed of SubBlocks) of the block */
    private int nonce; /** arbitrary number to be used in cryptography */
    private String lastSubBlockHash; /** contains the hash of the last sub block */

    public Block(String previousHashBlock, int prefix, T[] seq_number, R[] transactions) throws IOException {
        this.previousHashBlock = previousHashBlock;

        putData(seq_number, transactions);

        this.timestamp = new Date().getTime();
        this.hashBlock = calcHash();
        mineBlock(prefix);
    }

    /**
     * Fill the hashmap with SubBlocks and their hash. Each meta_data and each content corresponds to a SubBlock.
     *
     * @param meta_data Array meta_data(s) of the information in a SubBlock.
     * @param content Array of the information of the SubBlock.
     */
    private void putData(T[] meta_data, R[] content) {

        data = new HashMap<>();
        String previousHash = "0";

        for (int i = 0; i < meta_data.length; i++) {
            SubBlock newSubBlock = new SubBlock(previousHash, meta_data[i], content[i]);
            previousHash = newSubBlock.getHashBlock();

            data.put(previousHash, newSubBlock);
        }
    }

    /**
     * It evaluates the Hash of the Data (hashMap) as following:
     * it is a long string of key + content, key + content, ..., key + content and it is hashed.
     *
     * @return the hash evaluated
     */
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
     * This is the hash puzzle that in fact has to be solved. It allows to restrict the domain of possible
     * hashes and to make the mining more difficult.
     *
     * @param prefix number of 0's that the hash has to begin with
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

        String result = "";

        String previous = lastSubBlockHash;

        while(!previous.equals("0")) {

            SubBlock subBlock = data.get(previous);

            result = "<- " + previous + " || " + subBlock.getMeta_Data() + " | " +
                    subBlock.getContent() + " | " + result;
        }

        return "[ " + result + " ]";
    }
}
