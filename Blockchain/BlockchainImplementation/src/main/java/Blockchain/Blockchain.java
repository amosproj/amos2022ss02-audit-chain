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
    public void addABlock(String sequence_number, String message) {
        Block block;

        if (this.blockchain.size() == 0)
            block = new Block("0", sequence_number, message, prefix); //GENESYS BLOCK
        else
            block = new Block(this.getLastBlockHash(), sequence_number, message, prefix);

        this.lastBlockHash = block.getHashBlock();

        blockchain.put(lastBlockHash, block);
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

        System.out.println(" <- ( " + current.getHashBlock() + " | " + current.getSequenceNumber() + " | " +
                current.getMessage() + " )");

    }
}
