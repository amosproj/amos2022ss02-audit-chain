import java.util.*;

public class BlockChain /*implements List<Block>*/ {

    private List<Block> blockchain;

    public BlockChain() {
        this.blockchain = new ArrayList<>();
    }

    /**
     * Add a new block to the chain
     *
     * @param content content of the block
     * @param prefix number of 0's in the beginning of the hash
     */
    public void addABlock(String content, int prefix) {

        String prefixString = new String(new char[prefix]).replace('\0', '0');

        Block newBlock = new Block(
                blockchain.get(blockchain.size() - 1).getHashBlock(),
                content);
        newBlock.mineBlock(prefix);

        blockchain.add(newBlock);
    }

}
