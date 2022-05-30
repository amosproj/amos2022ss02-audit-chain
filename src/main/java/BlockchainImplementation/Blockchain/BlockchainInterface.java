package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;

import java.io.File;

public interface BlockchainInterface {

    public void addABlock(String path, String filename);

    public String getLastBlockHash();

    public Block getBlockFromHash(String hashBlock);


}
