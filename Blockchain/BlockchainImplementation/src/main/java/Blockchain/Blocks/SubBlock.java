package Blockchain.Blocks;

import Blockchain.Hashing.Hasher;

import java.util.Date;

public class SubBlock {

    private final String previousHashBlock; /** contains the hash of the previous block */
    private String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    private final String sequence_number; /** contains the sequence number of the message */
    private final String message; /** content of the current block */

    public SubBlock(String previousHashBlock, String sequence_number, String message) {
        this.previousHashBlock = previousHashBlock;
        this.sequence_number = sequence_number;
        this.message = message;
        this.timestamp = new Date().getTime();
        this.hashBlock = calcHash();
    }


    private String calcHash() {
        return Hasher.hashSHA256(previousHashBlock, Long.toString(timestamp), sequence_number, message);
    }

    public String getHashBlock () {
        return hashBlock;
    }

    public String getPreviousHashBlock () { return previousHashBlock; }

    public String getSequenceNumber () { return sequence_number; }

    public String getMessage () { return message; }

    public long getTimestamp () { return timestamp; }

}
