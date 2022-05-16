import java.util.*;

public class BlockChain /*implements List<Block>*/ {

    private List<Block> blockchain;

    public BlockChain() {
        this.blockchain = new ArrayList<>();
    }


    public void addABlock(String content, int prefix) {

        String prefixString = new String(new char[prefix]).replace('\0', '0');

        Block newBlock = new Block(
                blockchain.get(blockchain.size() - 1).getHashBlock(),
                content);
        newBlock.mineBlock(prefix);

        blockchain.add(newBlock);
    }

}
