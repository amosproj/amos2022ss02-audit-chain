package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBlock<T> {

    private final String previousHashBlock; /** contains the hash of the previous block */
    protected String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    protected final T transaction; /** contains the transaction data */

    public AbstractBlock(String previousHashBlock, T transaction) throws IOException {
        this.previousHashBlock = previousHashBlock;
        this.transaction = transaction;

        this.timestamp = new Date().getTime();
        this.hashBlock = calcHash();
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


//    public String toString() {
//
//        String result = "";
//
//        String previous = lastSubBlockHash;
//
//        while(!previous.equals("0")) {
//
//            SubBlock subBlock = data.get(previous);
//
//            result = "<- " + previous + " || " + subBlock.getMeta_Data() + " | " +
//                    subBlock.getContent() + " | " + result;
//        }
//
//        return "[ " + result + " ]";
//    }
}
