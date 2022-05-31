package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;

import java.io.File;

public interface BlockchainInterface<T,R> {

    public void addABlock(T[] meta_data, R[] content);

    public String getLastBlockHash();

    public Block getBlockFromHash(String hashBlock);


}
