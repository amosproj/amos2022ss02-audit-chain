package Blockchain;

import Blockchain.Blocks.Block;

public interface BlockchainInterface {

    public void addABlock(String sequence_number, String message);

    public String getLastBlockHash();

    public Block getBlockFromHash(String hashBlock);


}
