package Blockchain;

import Blockchain.Blocks.Block;

import java.util.HashMap;
import java.util.Map;

public class Blockchain implements BlockchainInterface {

    private Map<String, Block> blockchain;
    private int prefix;
    private String lastBlockHash;

    public Blockchain(int prefix) {
        this.prefix = prefix;
        this.blockchain = new HashMap<>();
    }


    @Override
    public void addABlock(String path, String filename) {
        Block block;

        try {
            if (this.blockchain.size() == 0)
                block = new Block("0", prefix, path, filename); //GENESYS BLOCK
            else
                block = new Block(this.getLastBlockHash(), prefix, path, filename);

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

        System.out.println(" <- ( " + current.getHashBlock() + " | " + current.getHashBlock() + " )");

    }
}
