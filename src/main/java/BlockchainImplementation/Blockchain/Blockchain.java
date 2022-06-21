package BlockchainImplementation.Blockchain;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;

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
     * Checks if the blockchain has tempered messages inside and returns a list of the involved subBlocks.
     * If a Block does not correspond to the hash saved anymore its all subBlocks are considered tempered and get returned
     * with other possible tempered subBlocks.
     *
     * @return a list of subBlocks with the tempered messages; the list is empty if there is none.
     */
    public List<SubBlock<T, R>> getTemperedMessageIfAny () {

        return getTemperedMessageIfAny(this.lastBlockHash, "0");

    }

    /**
     * Checks if the blockchain has tempered messages inside between the intervals defined with the Hash of the blocks
     * and returns a list of the involved subBlocks.
     * If a Block does not correspond to the hash saved anymore its all subBlocks are considered tempered and get returned
     * with other possible tempered subBlocks.
     *
     * @param hashStart the hash of the first block of the interval (included)
     * @param hashEnd the hash of the last block of the interval (excluded)
     *
     * @throws IllegalArgumentException if the hashStart is before the hashEnd
     *
     * @return a list of subBlocks with the tempered messages; the list is empty if there is none.
     */
    public List<SubBlock<T, R>> getTemperedMessageIfAny (String hashStart, String hashEnd) {

        List<SubBlock<T, R>> temperedMessage = new ArrayList<>();

        String hash = hashStart;

        while(!hash.equals("0") && !hash.equals(hashEnd)) {

            List<SubBlock<T, R>> tempered = this.blockchain.get(hash).getTemperedMessageIfAny();

            if(tempered.size() == 0)
                if(!hash.equals(this.blockchain.get(hash).getHashBlock()))
                    tempered.addAll(this.blockchain.get(hash).getTransaction().values());

            temperedMessage.addAll(tempered);

            hash = this.blockchain.get(hash).getPreviousHashBlock();

        }

        if(hash.equals("0") && !hashEnd.equals("0"))
            throw new IllegalArgumentException("The interval is not valid; HashStart is before HashEnd");

        return temperedMessage;
    }

    /**
     * Checks if the blockchain contains a specific message and if it results as tempered or if one of its subBlocks
     * do. If it is the case a list of the involved subBlocks is returned. If all the subBlocks result authentic but the
     * hash of the block does not correspond with the one saved in the block or the one saved in the hashMap then the whole
     * block is considered as tempered.
     *
     * @param meta_data the meta_data of the block to find
     * @param content the content of the block to find
     *
     * @return a list of subBlocks that belongs to the block with that meta_data and content with the tempered messages;
     *          the list is empty if none of its subBlocks result as tempered; The list is null if the block has not been found.
     *
     */
    public List<SubBlock<T, R>> getTemperedMessageFromABlockIfAny (T[] meta_data, R[] content) {

        List<SubBlock<T, R>> temperedMessage = null;

        for(String hash : this.blockchain.keySet()) {

            Block<T,R> b = this.blockchain.get(hash);

            String prevHash = b.getPreviousHashBlock();
            Block<T,R> block = new Block<>(prevHash, meta_data, content);

            if(block.getHashBlock().equals(b.getHashBlock()) || block.getHashBlock().equals(hash)) {

                temperedMessage = b.getTemperedMessageIfAny();

                if(temperedMessage.size() == 0)
                    if(!block.getHashBlock().equals(b.getHashBlock()) || !block.getHashBlock().equals(hash))
                        temperedMessage.addAll(b.getTransaction().values());

                break;
            }

        }

        return temperedMessage;
    }

    /**
     * Checks if the blockchain contains a specific message obtained from a file and if it results as tempered or if one of its subBlocks
     * do. If it is the case a list of the involved subBlocks is returned. If all the subBlocks result authentic but the
     * hash of the block does not correspond with the one saved in the block or the one saved in the hashMap then the whole
     * block is considered as tempered.
     *
     * The file has to be of a specific type such that the first line is the meta_data of the first message,
     * the second line is the content of the first message and third line is the hmac of the first message, then this
     * is repeated for the second message and so on.
     *
     * @param file the file which contains the data of the block to find.
     *
     * @return a list of subBlocks that belongs to the block with that meta_data and content with the tempered messages;
     *          the list is empty if none of its subBlocks result as tempered; The list is null if the block has not been found.
     *
     */
    public List<SubBlock<T, R>> getTemperedMessageFromABlockIfAny (File file) {

        List<String> linesFromFile = Collections.emptyList();

        try{
            linesFromFile =  Files.readAllLines(file.toPath());
        } catch (Exception e) {
            System.out.println("File Not Found");
        }

        T[] meta_data = (T[]) new String[linesFromFile.size()/3];
        R[] content = (R[]) new String[linesFromFile.size()/3];

        for (int i = 0; i < linesFromFile.size(); i++)
            if(i % 3 == 0)
                meta_data[i/3] = (T) linesFromFile.get(i);
            else
                if(i % 3 == 1)
                    content[i/3] = (R) linesFromFile.get(i);
                else { /* hmacData useless now */ }

        return getTemperedMessageFromABlockIfAny(meta_data, content);

    }

    /**
     * Checks if the blockchain contains a specific message obtained from a file in a specific path and if it results as tempered or if one of its subBlocks
     * do. If it is the case a list of the involved subBlocks is returned. If all the subBlocks result authentic but the
     * hash of the block does not correspond with the one saved in the block or the one saved in the hashMap then the whole
     * block is considered as tempered.
     *
     * The file has to be of a specific type such that the first line is the meta_data of the first message,
     * the second line is the content of the first message and third line is the hmac of the first message, then this
     * is repeated for the second message and so on.
     *
     * @param path the path of the file which contains the data of the block to find.
     *
     * @return a list of subBlocks that belongs to the block with that meta_data and content with the tempered messages;
     *          the list is empty if none of its subBlocks result as tempered; The list is null if the block has not been found.
     *
     */
    public List<SubBlock<T, R>> getTemperedMessageFromABlockIfAny (String path) {

        return getTemperedMessageFromABlockIfAny(new File(path));

    }


    /**
     * Parses the blockchain and saves it in a JSON file. The file will be saved in the current directory.
     * If the path is not found, a message is shown.
     */
    public void blockchainToJson(String path) {

        Gson gson = new Gson(); 
        String jsonBlockchain = gson.toJson(this);
        Path pathP = Paths.get(path);

        try{
            pathP = Files.writeString(pathP, jsonBlockchain, StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);
        } 
        catch(IOException e){//maybe we need to add an Exception e
            System.out.println("Sorry, wrong path");
        }

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

        } catch(FileNotFoundException e){
            System.out.println("The file was not found, please check the path again.");
        }catch (IOException f){
            System.out.println("Blockchain does not exist yet");
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
