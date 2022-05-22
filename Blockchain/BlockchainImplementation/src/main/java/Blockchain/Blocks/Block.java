package Blockchain.Blocks;

import Blockchain.Hashing.Hasher;

import java.util.Date;

public class Block implements BlockInterface {

    private final String previousHashBlock; /** contains the hash of the previous block */
    private String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    private final String sequence_number; /** contains the sequence number of the message */
    private final String message; /** content of the current block */
    private int nonce; /** arbitrary number to be used in cryptography */

    public Block(String previousHashBlock, String sequence_number, String message, int prefix) {
        this.previousHashBlock = previousHashBlock;
        this.sequence_number = sequence_number;
        this.message = message;
        this.timestamp = new Date().getTime();
        this.hashBlock = calcHash();
        mineBlock(prefix);
    }

    /**
     * Calculates the hash of the current block calling {@link Hasher#hashSHA256} method
     *
     * @return the hash of the current block
     * */
    private String calcHash() {
        return Hasher.hashSHA256(previousHashBlock, Long.toString(timestamp),
                Integer.toString(nonce), sequence_number, message);
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
    @Override
    public String getHashBlock () {
        return hashBlock;
    }

    @Override
    public String getPreviousHashBlock () { return previousHashBlock; }

    public String getSequenceNumber () { return sequence_number; }

    public String getMessage () { return message; }

    public long getTimestamp () { return timestamp; }



}
