package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;

import java.io.IOException;
import java.util.Date;

/**
 * Abstract class for all type of blocks that belong to a blockchain. It is characterized by the fact that carries the
 * transaction data and the hash of the previous block.
 *
 * @param <T> The type of the transaction data.
 */
public abstract class AbstractBlock<T> {

    private final String previousHashBlock; /** contains the hash of the previous block */
    protected String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    protected final T transaction; /** contains the transaction data */

    public AbstractBlock(String previousHashBlock, T transaction) {
        this.previousHashBlock = previousHashBlock;
        this.transaction = transaction;

        this.timestamp = new Date().getTime();
    }

    /**
     * Calculates the hash of the current block calling {@link Hasher#hashSHA256} method
     *
     * @return the hash of the current block
     * */
    protected String calcHash() {
        return Hasher.hashSHA256(previousHashBlock, Integer.toString(transaction.hashCode()));
    }

    /**
     * @return hash of the current block saved in the block
     */
    public String getHashBlock () {
        return hashBlock;
    }

    public String getPreviousHashBlock () { return previousHashBlock; }

    public T getTransaction () { return transaction; }

    public long getTimestamp () { return timestamp; }

    /**
     * Checks if the current block has been tempered or instead if it is still authentic.
     *
     * @return true if the current block is authentic, false otherwise
     */
    public boolean isBlockAuthentic () {
        return hashBlock.equals(calcHash());
    }

}
