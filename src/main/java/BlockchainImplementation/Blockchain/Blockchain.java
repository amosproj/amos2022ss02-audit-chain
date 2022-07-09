package BlockchainImplementation.Blockchain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import com.google.gson.reflect.TypeToken;

import static java.nio.file.StandardOpenOption.*;

/**
 * Data structure that represents the blockchain. It contains a hashmap of Blocks in which transactions and their
 * meta_data are stored.
 *
 * @param <T> the type of the meta_data of the transactions
 * @param <R> the type of the content of the transactions
 */
public class Blockchain<T,R> implements BlockchainInterface<T,R> {

    protected int numberBlockchain; /** Number which indicates which part of the blockchain this is */
    protected Map<String, Block<T,R>> blockchain; /** Map of blocks and their hash */
    protected String lastBlockHash; /** The hash of the last block of the blockchain */
    protected boolean locked; /** It is false if the current part of the blockchain can be modified, true otherwise */
    protected String path; /** Path of the directory where the blockchain will save itself */
    protected long maxByte; /** Maximum size in bytes of each file of the blockchain */

    public Blockchain(String pathDirectory, long maxSizeByte) {

        this.path = pathDirectory;
        this.maxByte = maxSizeByte;

        try {

            jsonToBlockchain();

        } catch (Exception e) {

            this.numberBlockchain = 1;
            this.lastBlockHash = "0";
            this.locked = false;
            this.blockchain = new HashMap<>();

        }

    }

    /**
     * Adds a block to the blockchain. If it is the first, it will be he genesys block and will point to "0".
     *
     * @throws RuntimeException if the blockchain is locked and someone tries to edit it
     *
     * @param meta_data the meta_data of the transactions in the block
     * @param content the content of the transactions in the block
     */
    @Override
    public void addABlock(T[] meta_data, R[] content) {

        if(locked)
            throw new RuntimeException("The current part of the blockchain is locked because it is not the latest part of it");

        Block<T,R> block;

        block = new Block<>(this.getLastBlockHash(), meta_data, content);

        this.lastBlockHash = block.getHashBlock();

        blockchain.put(lastBlockHash, block);

        blockchainToJson();
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

        this.blockchainToJson(Long.MAX_VALUE);

        while(!hash.equals("0") && !hash.equals(hashEnd)) {

            List<SubBlock<T, R>> tempered = this.blockchain.get(hash).getTemperedMessageIfAny();

            if(tempered.size() == 0)
                if(!hash.equals(this.blockchain.get(hash).getHashBlock()))
                    tempered.addAll(this.blockchain.get(hash).getTransaction().values());

            temperedMessage.addAll(tempered);

            hash = this.blockchain.get(hash).getPreviousHashBlock();

            if(hash.equals("0")) {

                try{
                    loadPreviousPartBlockchain();
                    hash = this.getLastBlockHash();
                } catch (RuntimeException ignored) {}

            }

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
        boolean found = false;

        while(!found) {

            for(String hash : this.blockchain.keySet()) {

                Block<T,R> b = this.blockchain.get(hash);

                String prevHash = b.getPreviousHashBlock();
                Block<T,R> block = new Block<>(prevHash, meta_data, content);

                if(block.getHashBlock().equals(b.getHashBlock()) || block.getHashBlock().equals(hash)) {

                    found = true;

                    temperedMessage = b.getTemperedMessageIfAny();

                    if(temperedMessage.size() == 0)
                        if(!block.getHashBlock().equals(b.getHashBlock()) || !block.getHashBlock().equals(hash))
                            temperedMessage.addAll(b.getTransaction().values());

                    break;
                }

            }

            if(!found) {
                try {
                    loadPreviousPartBlockchain();
                } catch (RuntimeException ignored) {
                    found = true;
                }
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
     * It finds the number of the last part of the blockchain reached which is saved in /lastBlockchain.txt
     *
     * @throws RuntimeException if there is no part of blockchain
     */
    private int findLastBlockchainNumber () {

        String lastBlockchain = "";
        int nLastBlockchain = 1;

        try {
            Path path = Path.of(this.path + "/lastBlockchain.txt");
            List<String> content = Files.readAllLines(path, StandardCharsets.UTF_8);
            lastBlockchain =  content.stream().collect(Collectors.joining());
            nLastBlockchain = Integer.parseInt(lastBlockchain);
        } catch (IOException e) {
            throw new RuntimeException("No previous blockchains have been found");
        }

        return nLastBlockchain;
    }

    /**
     * It loads the previous part of the blockchain (current number part - 1)
     *
     * @throws RuntimeException if it has already reached the beginning
     */
    public void loadPreviousPartBlockchain () throws RuntimeException {
        if(numberBlockchain != 1)
            restoreBlockchainFromJson(numberBlockchain-1);
        else
            throw new RuntimeException("The beginning of the blockchain has been reached");
    }

    /**
     * It loads the next part of the blockchain (current number part + 1)
     *
     * @throws RuntimeException if it has already reached the end
     */
    public void loadNextPartBlockchain () throws RuntimeException{

        int nLastBlockchain = findLastBlockchainNumber();

        if(nLastBlockchain == 0) {
            System.out.println("No previous blockchains have been found");
            return;
        }

        if(numberBlockchain != nLastBlockchain)
            restoreBlockchainFromJson(numberBlockchain+1);
        else
            throw new RuntimeException("The end of the blockchain has already been reached");
    }

    private Block<T,R> getFirstBlock () {

        String hash = lastBlockHash;
        Block<T,R> block = null;

        while(!hash.equals("0")) {
            block = blockchain.get(hash);

            hash = block.getPreviousHashBlock();
        }

        return block;
    }

    private List<T> getFirstAndLastSN() {
        List<T> firstAndLast = new ArrayList<>();

         SubBlock<T,R> first = getFirstBlock().getTransaction().get(getFirstBlock().getFirstSubBlockHash());
         SubBlock<T,R> last = blockchain.get(getLastBlockHash()).getTransaction().get(blockchain.get(getLastBlockHash()).getLastSubBlockHash());

         firstAndLast.add(first.getMeta_Data());
         firstAndLast.add(last.getMeta_Data());

        return firstAndLast;
    }

    /**
     * Parses the blockchain and saves it in a JSON file. The file will be saved in the current directory described by
     * pathDirectory and if the size of the file is bigger than limitBySize, a new part of the blockchain will be created to
     * work on. If the path is not found, a message is shown.
     *
     * @param limitByteSize limit in byte of the size of the file of the blockchain that can reach. It will for sure exceeded
     *                      but after it a new part for the blockchain to work on will be created.
     */
    public void blockchainToJson(long limitByteSize) {

        if(locked) {
            throw new IllegalStateException("This part of the blockchain is currently locked and cannot be overrided");
        }

        if(lastBlockHash.equals("0")) {
            System.out.println("The Blockchain is still too empty to be saved");
            return;
        }

        Gson gson = new Gson();

        Type tType = new TypeToken<Blockchain<T,R>>() {}.getType();

        String pathDirectory1 = this.path + "/blockchain" + numberBlockchain + ".json";
        String pathDirectory2 = this.path + "/lastBlockchain.txt";
        String pathDirectory3 = this.path + "/sequenceRecords.csv";

        String jsonBlockchain = gson.toJson(this, tType);
        Path path = Paths.get(pathDirectory1);
        Path path2 = Paths.get(pathDirectory2);
        Path path3 = Paths.get(pathDirectory3);

        try{
            Files.writeString(path, jsonBlockchain, StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);
            Files.writeString(path2, String.valueOf(numberBlockchain), StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);

            long bytes = Files.size(path);

            if(bytes > limitByteSize) {
                this.locked = true;
                jsonBlockchain = gson.toJson(this, tType);
                Files.writeString(path, jsonBlockchain, StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);

                if(numberBlockchain == 1) {
                    Files.writeString(path3, "NUMBER_FILE,START_SEQNUMBER,END_SEQNUMBER\n", StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);
                }

                String csvLine = numberBlockchain + "," + getFirstAndLastSN().get(0) + "," + getFirstAndLastSN().get(1) + "\n";
                Files.writeString(path3, csvLine, StandardCharsets.UTF_8, APPEND);


                numberBlockchain++;
                lastBlockHash = "0";
                blockchain = new HashMap<>();
                this.locked = false;
            }
        } 
        catch(IOException e){
            System.out.println("Sorry, wrong path");
        }

    }

    /**
     * Parses the blockchain and saves it in a JSON file. The file will be saved in the current directory described by
     * pathDirectory and if the size of the file is bigger than maxSize defined in the blockchain, a new part of the blockchain will be created to
     * work on. If the path is not found, a message is shown.
     *
     */
    public void blockchainToJson() {
        blockchainToJson(maxByte);
    }

    /**
     * Takes the last part of the blockchain contained in a json file and load it.
     *
     * @throws RuntimeException if no blockchain has been found
     */
    public void jsonToBlockchain() throws RuntimeException{

        int nLastBlockchain = findLastBlockchainNumber();
        restoreBlockchainFromJson(nLastBlockchain);

    }

    /**
     * Takes the nLastBlockchain-th part of the blockchain contained in a json file and load it.
     * If the file is not found, a message is shown (it could also be that no blockchains have already been stored there before)
     *
     * @param nBlockchain represents the n-th part of the blockchain that it is needed to load
     */
    public void restoreBlockchainFromJson(int nBlockchain) {

        Blockchain<T,R> blockchainFromJson = jsonToBlockchain(nBlockchain);

        this.blockchain = blockchainFromJson.blockchain;
        this.lastBlockHash = blockchainFromJson.lastBlockHash;
        this.locked = blockchainFromJson.locked;
        this.numberBlockchain = nBlockchain;

    }

    protected Blockchain<T,R> jsonToBlockchain(int nBlockchain) {

        Gson gson =  new Gson();

        Blockchain<T, R> blockchainFromJson = new Blockchain<>("/", Long.MAX_VALUE);
        FileReader fileReader = null;

        String pathDirectory = this.path;

        pathDirectory += "/blockchain" + nBlockchain + ".json";

        try {
            fileReader = new FileReader(pathDirectory);

            blockchainFromJson = (Blockchain<T,R>) gson.fromJson(fileReader, Blockchain.class);

            fileReader.close();

        } catch(FileNotFoundException e){
            System.out.println("Errors trying to find the part " + nBlockchain + " of the Blockchain");
        }catch (IOException f){
            System.out.println("Error reading the file");
        }

        return blockchainFromJson;
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

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

}
