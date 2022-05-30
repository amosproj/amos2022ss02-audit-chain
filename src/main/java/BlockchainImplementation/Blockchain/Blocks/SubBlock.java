package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;

import java.util.Date;

public class SubBlock<T,R> {

    private final String previousHashBlock; /** contains the hash of the previous block */
    private String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    private final T meta_data; /** contains the sequence number of the message */
    private final R content; /** content of the current block */

    public SubBlock(String previousHashBlock, T sequence_number, R message) {
        this.previousHashBlock = previousHashBlock;
        this.meta_data = sequence_number;
        this.content = message;
        this.timestamp = new Date().getTime();
        this.hashBlock = calcHash();
    }


    private String calcHash() {
        return Hasher.hashSHA256(previousHashBlock, Long.toString(timestamp), meta_data.toString(), content.toString());
    }

    public String getHashBlock () {
        return hashBlock;
    }

    public String getPreviousHashBlock () { return previousHashBlock; }

    public T getMeta_Data () { return meta_data; }

    public R getContent() { return content; }

    public long getTimestamp () { return timestamp; }

}
