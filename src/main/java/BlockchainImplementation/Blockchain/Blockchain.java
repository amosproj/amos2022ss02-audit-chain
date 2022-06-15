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
import com.google.gson.Gson;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Data structure that represents the blockchain. It contains a hashmap of Blocks in which transactions and their
 * meta_data are stored.
 *
 * @param <T> the type of the meta_data of the transactions
 * @param <R> the type of the content of the transactions
 */
public class Blockchain<T,R> implements BlockchainInterface<T,R> {

    private Map<String, Block<T,R>> blockchain; /** Map of blocks and their hash */
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
    
    /**
     * Parses the blockchain and saves it in a JSON file. The file will be saved in the current directory.
     * If the path is not found, a message is shown.
     */
    public void blockchainToJson(){
        Gson gson = new Gson(); 
        String jsonBlockchain = gson.toJson(this);
        Path path = Paths.get("the-file-name.json");

        try{
            path = Files.writeString(path, jsonBlockchain, StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);
        } 
        catch(IOException e){//maybe we need to add an Exception e
            System.out.println("Sorry, wrong path");
        }

//        path.toFile().setReadOnly(); //creates problems
    }

    /**
     * Takes the json file contained in the path and parses it into a blockchain.
     * If the file is not found, a message is shown and it means that is the first time that the blockchain has been saved
     *
     * @param path represents the path where the json file is located
     */
    public void jsonToBlockchain(Path path) {
        Gson gson = new Gson();
        Blockchain<T, R> blockchainFromJson = new Blockchain<>();

        FileReader fileReader = null;
        try {

            fileReader = new FileReader(path.toFile());

            // Convert JSON File to Java Object
            Object obj = gson.fromJson(fileReader, Blockchain.class);
            blockchainFromJson = (Blockchain<T, R>) obj;

            fileReader.close();

        } catch (IOException e) {
            System.out.println("Blockchain does not exist yet or the path is wrong");
        }

        this.blockchain = blockchainFromJson.blockchain;
        this.lastBlockHash = blockchainFromJson.lastBlockHash;

    }

    @Override
    public boolean equals (Object obj) {

        if(!(obj instanceof Blockchain))
            return false;

        Blockchain<T, R> blockchain = (Blockchain<T, R>) obj;

        if (this.blockchain.size() != blockchain.blockchain.size())
            return false;

        if(!this.lastBlockHash.equals(blockchain.lastBlockHash))
            return false;

        for(String hash : this.blockchain.keySet())
            if(!this.blockchain.get(hash).equals(blockchain.blockchain.get(hash)))
                return false;

        return true;
    }
}
