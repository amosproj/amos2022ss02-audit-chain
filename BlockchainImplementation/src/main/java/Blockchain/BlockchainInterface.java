package Blockchain;

import Blockchain.Blocks.Block;

public interface BlockchainInterface {

    public void addABlock(String path, String filename);

    public String getLastBlockHash();

    public Block getBlockFromHash(String hashBlock);


}
