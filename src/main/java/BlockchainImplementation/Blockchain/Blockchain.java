package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

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
    public List<SubBlock<T, R>> getTemperedMessageIfAny () {

        List<SubBlock<T, R>> temperedMessage = new ArrayList<>();

        for (Block<T,R> block : this.blockchain.values()) {

            List<SubBlock<T, R>> tempered = block.getTemperedTransaction();

            temperedMessage.addAll(tempered);

        }

        return temperedMessage;
    }

//    /**
//     * Checks if a given block under the form of a File is inside the blockchain and has been tempered.
//     * It does this by highlighting all possible tempered (suspicious) blocks. A block is highlighted if something inside it
//     * is not authentic with at least more than one transaction in common with the file given. Then the block chosen
//     * (amond the highlighted ones) is the one with the highest number of transactions in common with the file.
//     * In the end this one is again analyzed and only its messages (as SubBlocks) that resulted as tempered are returned
//     * as a list.
//     *
//     * @param file the block to check under the form of a file
//     *
//     * @return a list of the messages that have been tempered
//     * @return null if the file is authentic or not found in the blockchain (so even though a block was suspicious,
//     *        there were 0 transactions in common with the one in the file) or if the file resulted as tempered but not
//     *        its content
//     *
//     */
//    public List<SubBlock<T,R>> getTemperedMessageIfAnyFromAFile(File file)  {
//
//        Block<T, R> block = fromFileToBlock(file);
//        String hmacData = block.calcHmacData();
//
//        List<Object[]> possibleTemperedBlocks = new ArrayList<>();
//
//        Block<T, R> lastBlock = getBlockFromHash(lastBlockHash);
//
//        while(!lastBlock.getPreviousHashBlock().equals("0")) {
//
//            if(!lastBlock.isBlockAuthentic()) {
//
//                if(hmacData.equals(lastBlock.calcHmacData())) {
//                    return null; //file content is authentic but its hash do not correspond with the one in the blockchain
//                } else {
//
//                    int similarTransactions = howManySimilarTransaction(block, lastBlock);
//
//                    if(similarTransactions > 0) {
//                        //not authentic
//
//                        possibleTemperedBlocks.add(new Object[]{similarTransactions, lastBlock});
//
//                    }
//                }
//
//            }
//
//            lastBlock = getBlockFromHash(lastBlock.getPreviousHashBlock());
//
//        }
//
//        if(possibleTemperedBlocks.size() == 0) {
//            return null; //file was not found in the blockchain
//        }
//
//        Object[] max = Collections.max(possibleTemperedBlocks, Comparator.comparingInt(o -> (int) o[0])); //find the block with the most similar transactions
//        Block<T,R> temperedBlock =  (Block<T, R>) max[1];
//
//        return getTemperedMessageFromBlock(block, temperedBlock);
//
//    }
//
//    private Block<T, R> fromFileToBlock (File file) {
//
//        List<String> result = Collections.emptyList();
//
//        try{
//            result =  Files.readAllLines(file.toPath());
//        } catch (Exception e) {
//            System.out.println("File Not Found");
//        }
//
//        T[] meta_data = (T[]) new String[result.size()];
//        R[] content = (R[]) new String[result.size()];
//
//        for (int i = 0; i < result.size(); i++)
//            if(i % 2 == 0)
//                meta_data[i] = (T) result.get(i);
//            else
//                content[i] = (R) result.get(i);
//
//        Block<T, R> block = new Block<>("0", meta_data, content);
//
//        return block;
//
//    }
//
//    private int howManySimilarTransaction (Block<T, R> block1, Block<T, R> block2) {
//
//        int counter = 0;
//
//        for(String hash : block1.getTransaction().keySet()) {
//            if(block2.getTransaction().get(hash) != null) {
//                counter++;
//            }
//        }
//
//        return counter;
//
//    }
//
//    private List<SubBlock<T,R>> getTemperedMessageFromBlock (Block<T, R> block, Block<T, R> temperedBlock) {
//
//        List<SubBlock<T,R>> temperedSubBlocks = new ArrayList<>();
//
//        for(String temperedSubBlockHash : temperedBlock.getTransaction().keySet()) {
//            if(block.getTransaction().get(temperedSubBlockHash) == null ) {
//                temperedSubBlocks.add(temperedBlock.getTransaction().get(temperedSubBlockHash));
//            } else {
//                if(!block.getTransaction().get(temperedSubBlockHash).isBlockAuthentic())
//                    temperedSubBlocks.add(temperedBlock.getTransaction().get(temperedSubBlockHash));
//            }
//        }
//
//        return temperedSubBlocks;
//    }


}
