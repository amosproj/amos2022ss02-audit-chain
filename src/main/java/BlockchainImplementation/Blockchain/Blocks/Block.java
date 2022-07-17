package BlockchainImplementation.Blockchain.Blocks;

import java.util.*;

/**
 * A Block is the unit of the blockchain. It contains the pointer to its previous block, a hashmap of data in which the
 * SubBlocks are stored, and other information.
 *
 * @param <T> The type of the meta_data (it could be everything, e.g. sequence number) of the information contained
 *  *           in the SubBlock.
 * @param <R> The type of the information contained in the SubBlock.
 */
public class Block<T,R> extends AbstractBlock<Map<String, SubBlock<T,R>>>{

    private String firstSubBlockHash; /** The hash of the first SubBlock added*/
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
     *
     * @throws IllegalArgumentException If the length of the meta_data and the content arrays are not equal.
     */
    private void putData(T[] meta_data, R[] content) {

        if(meta_data.length != content.length)
            throw new IllegalArgumentException("There is one or more either meta_data or content of the message missing");

        String previousHash = "0";

        for (int i = 0; i < meta_data.length; i++) {
            SubBlock<T,R> newSubBlock = new SubBlock<>(previousHash, meta_data[i], content[i]);
            previousHash = newSubBlock.getHashBlock();

            transaction.put(previousHash, newSubBlock);

            if(i == 0) firstSubBlockHash = previousHash;
        }

        lastSubBlockHash = previousHash;
    }

    public String getLastSubBlockHash() {
        return lastSubBlockHash;
    }

    public String getFirstSubBlockHash() {
        return firstSubBlockHash;
    }

    /**
     * Checks if the block has tempered messages inside and returns a list of the involved subBlocks.
     * The message is considered tempered if one of these condition is satisfied:
     * - the subBlock in which it is contained is not authentic
     * - the subBlock associated to the key in the hashmap, results to have a different hash when calculated again
     * - the Block in which its subBlock is part results non-authentic (e.g. a non-authorized SubBlock (authentic) has been added to the block)
     *
     * @return a list of subBlocks with the tempered messages; the list is empty if there is none.
     */
    public List<SubBlock<T, R>> getTemperedMessageIfAny() {

        List<SubBlock<T, R>> temperedMessages = new ArrayList<>();

        for(String key : this.transaction.keySet())
            if(!this.transaction.get(key).isAuthentic() || !(key.equals(this.transaction.get(key).hashBlock)))
                temperedMessages.add(this.transaction.get(key));

        if(temperedMessages.size() == 0)
            if(!this.isAuthentic())
                for(String key : this.transaction.keySet())
                    temperedMessages.add(this.transaction.get(key));

        return temperedMessages;
    }

    public int getSize () {
        return this.transaction.size();
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

        for(String hash : this.transaction.keySet())
            if(!this.transaction.get(hash).equals(block.transaction.get(hash)))
                return false;

        return true;
    }

}
