package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Blockchain;
import BlockchainImplementation.Blockchain.Hashing.Hasher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A Block is the unit of the blockchain. It contains the pointer to its previous block, a hashmap of data in which the
 * SubBlocks are stored, and other information.
 *
 * @param <T> The type of the meta_data (it could be everything, e.g. sequence number) of the information contained
 *  *           in the SubBlock.
 * @param <R> The type of the information contained in the SubBlock.
 */
public class Block<T,R> extends AbstractBlock<Map<String, SubBlock<T,R>>>{

    private String lastSubBlockHash; /** The hash of the last SubBlock added*/

    public Block(String previousHashBlock, T[] meta_data, R[] content) {
        super(previousHashBlock, new HashMap<>());

        putData(meta_data, content);

        this.hashBlock = calcHash();
    }

    /**
     * Fill the hashmap with SubBlocks and their hash. Each meta_data and each content corresponds to a SubBlock.
     *
     * @param meta_data Array meta_data(s) of the information in a SubBlock.
     * @param content Array of the information of the SubBlock.
     */
    private void putData(T[] meta_data, R[] content) {

        String previousHash = "0";

        for (int i = 0; i < meta_data.length; i++) {
            SubBlock<T,R> newSubBlock = new SubBlock<>(previousHash, meta_data[i], content[i]);
            previousHash = newSubBlock.getHashBlock();

            transaction.put(previousHash, newSubBlock);
        }

        lastSubBlockHash = previousHash;
    }

    public String getLastSubBlockHash() {
        return lastSubBlockHash;
    }

    /**
     * {@inheritdoc}
     * Moreover checks if every SubBlock is also authentic or not.
     *
     * @return true if the current block is authentic, false otherwise
     */
    @Override
    public boolean isBlockAuthentic () {
        boolean authentic = true;

        for (SubBlock<T,R> subBlock : this.transaction.values()) {
            authentic = subBlock.isBlockAuthentic();

            if (!authentic) {
                break;
            }
        }

        authentic = authentic && hashBlock.equals(calcHash());

        return authentic;
    }

    @Override
    public boolean equals (Object obj) {

        if(!(obj instanceof Block))
            return false;

        Block<T, R> block = (Block<T, R>) obj;

        if(!lastSubBlockHash.equals(block.getLastSubBlockHash()))
            return false;

        if(!hashBlock.equals(block.getHashBlock()))
            return false;

        if(!getPreviousHashBlock().equals(block.getPreviousHashBlock()))
            return false;

        if(getTimestamp() !=(block.getTimestamp()))
            return false;

        for(String hash : this.transaction.keySet())
            if(!this.transaction.get(hash).equals(block.transaction.get(hash)))
                return false;

        return true;
    }

}
