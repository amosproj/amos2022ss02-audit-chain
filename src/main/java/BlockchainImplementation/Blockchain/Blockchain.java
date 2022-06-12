package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data structure that represents the blockchain. It contains a hashmap of Blocks in which transactions and their
 * meta_data are stored.
 *
 * @param <T> the type of the meta_data of the transactions
 * @param <R> the type of the content of the transactions
 */
public class Blockchain<T,R> implements BlockchainInterface<T,R> {

    private final Map<String, Block<T,R>> blockchain; /** Map of blocks and their hash */
    private String lastBlockHash; /** The hash of the last block of the blockchain */

    public Blockchain() {
        lastBlockHash = "0";

        this.blockchain = new HashMap<>();
    }

    /**
     * Adds a block to the blockchain. If it is the first, it will be he genesys block and will point to "0".
     *
     * @param meta_data the meta_data of the transactions in the block
     * @param content the content of the transactions in the block
     */
    @Override
    public void addABlock(T[] meta_data, R[] content) {
        Block<T,R> block;

        block = new Block<>(this.getLastBlockHash(), meta_data, content);

        this.lastBlockHash = block.getHashBlock();

        blockchain.put(lastBlockHash, block);


    }


    @Override
    public String getLastBlockHash() {
        return lastBlockHash;
    }

    @Override
    public Block getBlockFromHash(String hashBlock) {
        return this.blockchain.get(hashBlock);
    }

    /**
     * Checks if the blockchain has been tempered or instead if it is still authentic.
     *
     * @return true if the blockchain is authentic, false otherwise
     */
    public boolean isBlockchainAuthentic () {
        boolean authentic = true;

        for (Block<T,R> block : this.blockchain.values()) {
            authentic = block.isBlockAuthentic();

            if (!authentic) {
                break;
            }
        }

        return authentic;
    }

    public void isFileAuthentic(File file)  {
        List<String> result = Collections.emptyList();

        try{
            result =  Files.readAllLines(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        T[] meta_data = (T[]) new String[result.size()];
        R[] content = (R[]) new String[result.size()];

        for (int i = 0; i < result.size(); i++)
            if(i % 2 == 0)
                meta_data[i] = (T) result.get(i);
            else
                content[i] = (R) result.get(i);

        Block<T, R> block = new Block<>("0", meta_data, content);
        String hmac = block.calcHmacData();

        //pensa di storare il genesis
        //toccherebbe controllare blocco per blocco dal genesis se sono autentici e se corrisponde a quel file
        //se qualcuno non e' autentico va tenuto in considerazione

       //tieni conto che mahari il metodo is authentic dovrebbe cambiare e partire a ricalcolare tutto dall'inizio


    }

}
