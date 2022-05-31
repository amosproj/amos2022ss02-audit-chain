package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;

import java.util.Date;

/**
 * A SubBlock is the smallest unit of a Block, it contains the clear information and points to its previous hashed SubBlock.
 *
 * @param <T> The type of the meta_data (it could be everything, e.g. sequence number) of the information contained
 *           in the SubBlock.
 * @param <R> The type of the information contained in the SubBlock.
 */

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


    /**
     * Calculates the hash of the current SubBlock just putting all its a properties together
     *
     * @return the hash of the current SubBlock
     */
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
