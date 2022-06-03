package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;
import ProducerDummy.Messages.AggregateMessage;
import ProducerDummy.Messages.Message;
import ProducerDummy.Persistence.AggregateMessageFilePersistence;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * A Block is the unit of the blockchain. It contains the pointer to its previous block, a hashmap of data in which the
 * SubBlocks are stored, and other information. It has to be mined in order to be added to the blockchain. Which means
 * that a hash puzzle is solved in order to find the correct hash.
 *
 * @param <T> The type of the meta_data (it could be everything, e.g. sequence number) of the information contained
 *  *           in the SubBlock.
 * @param <R> The type of the information contained in the SubBlock.
 */
public class Block<T,R> extends AbstractBlock<Map<String, SubBlock<T,R>>>{

    private String lastSubBlockHash;

    public Block(String previousHashBlock, T[] meta_data, R[] content) throws IOException {
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
    private void putData(T[] meta_data, R[] content) throws IOException {

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



//    public String toString() {
//
//        String result = "";
//
//        String previous = lastSubBlockHash;
//
//        while(!previous.equals("0")) {
//
//            SubBlock subBlock = data.get(previous);
//
//            result = "<- " + previous + " || " + subBlock.getMeta_Data() + " | " +
//                    subBlock.getContent() + " | " + result;
//        }
//
//        return "[ " + result + " ]";
//    }
}
