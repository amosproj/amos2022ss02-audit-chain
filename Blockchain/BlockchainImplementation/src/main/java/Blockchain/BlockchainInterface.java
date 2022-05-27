package Blockchain;

import Blockchain.Blocks.Block;

import java.io.File;

public interface BlockchainInterface {

    public void addABlock(File transactions);

    public String getLastBlockHash();

    public Block getBlockFromHash(String hashBlock);


}
