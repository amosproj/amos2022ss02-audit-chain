package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson; //this is weird no? 

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

    public void blockchainToJson(Path path){
        Gson gson = new Gson(); 
        String jsonBlockchain = gson.toJson(this.blockchain); 
        try{
            Path newPath = Files.writeString(path, jsonBlockchain, StandardCharsets.UTF_8);  
            //in order to make the file secure against manipulation
            File content = newPath.toFile();
            content.setReadOnly(); 
        } 
        catch(IOException e){//maybe we need to add an Exception e
            System.out.println("Sorry, wrong path"); 
        }
    }

    public void jsonToBlockchain(Path path){
        Gson gson = new Gson(); 
        //alla fine ci deve essere qualcosa tipo this = quella che viene ripresa dal json
        //try catch: nel catch ok wrong path perchè la blockchain non esiste e quindi rimane vuota (non devi fare niente-> niente deve essere recuperato dal file bc there's nothing)
        try (Reader reader = new FileReader(path.toFile())) {

            // Convert JSON File to Java Object
            Blockchain blockchain = gson.fromJson(path.toFile().toString(), Blockchain.class);
		
			// print staff object
            System.out.println(blockchain);

        } catch (IOException e) {
            System.out.println("Sorry, wrong path - this means that the blockchain does not exist yet"); 
        }
    }

    public static void main(String [] args){
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                             new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                             new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                             new String[]{"g", "h", "i"});       
        blockchain.blockchainToJson(Paths.get("src", "main", "java","BlockchainImplementation")); 
        
    }

}
