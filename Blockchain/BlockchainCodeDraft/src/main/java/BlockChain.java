import java.util.*;

public class BlockChain /*implements List<Block>*/ {

    private List<Block> blockchain;
    private int prefix;
    String prefixString;

    public BlockChain() {
        this.blockchain = new ArrayList<>();
        this.prefix = 4;
        this. prefixString = new String(new char[prefix]).replace('\0', '0');
    }

    public void addABlock(String content) {
        Block newBlock = new Block(
                blockchain.get(blockchain.size() - 1).getHashBlock(),
                content);
        newBlock.mineBlock(prefix);

        blockchain.add(newBlock);
    }

}
