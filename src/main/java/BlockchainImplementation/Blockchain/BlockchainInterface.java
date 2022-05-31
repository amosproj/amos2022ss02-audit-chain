package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;

import java.io.File;

/**
 * Interface for a Blockchain which stores two type of information: meta data of transaction, content of transaction.
 *
 * @param <T> type of the meta data of transaction
 * @param <R> type of the content of transaction
 */
public interface BlockchainInterface<T,R> {

    public void addABlock(T[] meta_data, R[] content);

    public String getLastBlockHash();

    public Block getBlockFromHash(String hashBlock);

}
