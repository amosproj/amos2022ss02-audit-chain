package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;

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

    private Map<String, Block> blockchain; /** Map of blocks and their hash */
    private int prefix; /** The prefix of the possible hashes of the blockchain, so that its domain is restricted */
    private String lastBlockHash; /** The hash of the last block of the blockchain */

    public Blockchain(int prefix) {
        this.prefix = prefix;
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
        Block block;

        try {
            if (this.blockchain.size() == 0)
                block = new Block("0", prefix, meta_data, content); //GENESYS BLOCK
            else
                block = new Block(this.getLastBlockHash(), prefix, meta_data, content);

            this.lastBlockHash = block.getHashBlock();

            blockchain.put(lastBlockHash, block);
        } catch (Exception e) {
            System.out.println("Error occurred while trying to add a block"); //to be removed or changed
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

    public void printBlockchain() {
        printBlockchain(getBlockFromHash(getLastBlockHash()));
    }

    private void printBlockchain(Block current) {

        if (current == null) {
            System.out.println("0");
            return;
        }

        printBlockchain(getBlockFromHash(current.getPreviousHashBlock()));

        System.out.println(" <- ( " + current.getHashBlock() + " | " + current.toString() + " )");

    }
}
