package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Data structure that represents the blockchain. It contains a hashmap of Blocks in which transactions and their
 * meta_data are stored.
 *
 * @param <T> the type of the meta_data of the transactions
 * @param <R> the type of the content of the transactions
 */
public class Blockchain<T,R> implements BlockchainInterface<T,R> {

    private final Map<String, Block<T,R>> blockchain; /** Map of blocks and their hash */
    private String lastBlockHash; /** The hash of the last block of the blockchain */

    public Blockchain() {
        lastBlockHash = "0";

        this.blockchain = new HashMap<>();
    }

    /**
     * Adds a block to the blockchain. If it is the first, it will be he genesys block and will point to "0".
     *
     * @param meta_data the meta_data of the transactions in the block
     * @param content the content of the transactions in the block
     */
    @Override
    public void addABlock(T[] meta_data, R[] content) {
        Block<T,R> block;

        try {
            block = new Block<>(this.getLastBlockHash(), meta_data, content);

            this.lastBlockHash = block.getHashBlock();

            blockchain.put(lastBlockHash, block);

        } catch (Exception e) {
            throw new RuntimeException("Error while adding a block to the blockchain");
        }

    }


    @Override
    public String getLastBlockHash() {
        return lastBlockHash;
    }

    @Override
    public Block getBlockFromHash(String hashBlock) {
        return this.blockchain.get(hashBlock);
    }


}
