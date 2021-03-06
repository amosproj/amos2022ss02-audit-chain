package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;

/**
 * Abstract class for all type of blocks that belong to a blockchain. It is characterized by the fact that carries the
 * transaction data and the hash of the previous block.
 *
 * @param <R> The type of the transaction data.
 */
public abstract class AbstractBlock<R> {

    private final String previousHashBlock; /** contains the hash of the previous block */
    protected String hashBlock; /** contains the hash of the current block */
    protected final R transaction; /** contains the transaction data */

    public AbstractBlock(String previousHashBlock, R transaction) {
        this.previousHashBlock = previousHashBlock;
        this.transaction = transaction;

    }

    /**
     * Calculates the hash of the current block calling {@link Hasher#hashSHA256} method
     *
     * @return the hash of the current block
     * */
    protected String calcHash() {
        return Hasher.hashSHA256(previousHashBlock, transaction.toString());
    }

    /**
     * @return hash of the current block saved in the block
     */
    public String getHashBlock () {
        return hashBlock;
    }

    public String getPreviousHashBlock () { return previousHashBlock; }

    public R getTransaction () { return transaction; }

    /**
     * Checks if the current block has been tempered or instead if it is still authentic.
     * It does it checking if the saved hash of the block still corresponds to the one evaluated.
     *
     * @return true if the current block is authentic, false otherwise
     */
    public boolean isAuthentic () {
        return hashBlock.equals(calcHash());
    }
    
}
