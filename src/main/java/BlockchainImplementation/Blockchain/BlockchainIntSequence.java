package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Data structure that represents the blockchain. It contains a hashmap of Blocks in which transactions and their
 * meta_data of type Integer are stored. Meta_data as Integer allows to possibly use them as Sequence Numbers of the
 * transactions
 *
 * @param <R> the type of the content of the transactions
 */
public class BlockchainIntSequence<R> extends Blockchain<Integer, R>{


    public BlockchainIntSequence(String pathDirectory, long maxSizeByte) {
        super(pathDirectory, maxSizeByte);
    }

    /**
     * It finds the number of blockchain parts to load before reaching the sequence number wanted.
     * It uses a file in which the sequence numbers are stored for each part of the blockchain so that this search
     * is optimized.
     *
     * @param seqNumber the sequence number that it is wanted to be found
     *
     * @throws IllegalArgumentException if the sequence number is not found in any part of the blockchain
     *
     * @return the number of the part of the blockchain that contains the sequence number wanted
     *
     */
    private int howManyBlockchainPartBeforeReaching (int seqNumber) throws IllegalArgumentException {

        int output = 0;
        boolean found = false;
        String[] arrayCSV;

        try {

            File file = new File(this.path + "/sequenceRecords.csv");

            Scanner input = new Scanner(file);

            while (!found && input.hasNext()) {
                String line = input.nextLine();

                arrayCSV = line.split(",");

                if(arrayCSV[0].equals("NUMBER_FILE"))
                    continue;

                int left = Integer.parseInt(arrayCSV[1]);
                int right = Integer.parseInt(arrayCSV[2]);
                output = Integer.parseInt(arrayCSV[0]);

                if(left <= seqNumber && seqNumber <= right) {
                    found = true;
                }

            }

            input.close();

        } catch (Exception ex) {
            return 0;
        }

        if(!found) {
            restoreBlockchainFromJson();
            Block<Integer, R> lastB = blockchain.get(getLastBlockHash());
            SubBlock<Integer, R> lastSub = lastB.getTransaction().get(lastB.getLastSubBlockHash());
            output = this.numberBlockchain;

            int lastSeqNumber = lastSub.getMeta_Data();


            if(seqNumber > lastSeqNumber)
                throw new IllegalArgumentException("Sequence Number has not been found in this and before this part of the blockchain");
        }

        return this.numberBlockchain - output;
    }

    /**
     * Load previous or next parts of the blockchain starting from the current one
     *
     * @param partBeforeStart indicates the number of parts to skip before reaching the wanted one (included).
     *                        If this number is negative then it loads next parts, if it is positive then it loads previous parts.
     */
    private void loadPreviousOrNextPartsByANumber(int partBeforeStart) {

        if(partBeforeStart > 0)
            while(partBeforeStart > 0) {
                loadPreviousPartBlockchain();
                partBeforeStart--;
            }

        if(partBeforeStart < 0)
            while(partBeforeStart < 0) {
                loadNextPartBlockchain();
                partBeforeStart++;
            }

    }

    /**
     * Checks if the blockchain has tempered messages inside between the intervals defined with the sequence number
     * (integer meta_data) of the blocks and returns a list of the involved subBlocks.
     *
     * @param seq_start the hash of the first block of the interval (included)
     * @param seq_end the hash of the last block of the interval (included)
     *
     * @throws IllegalArgumentException if the seq_start appears before seq_end
     *
     * @return a list of subBlocks with the tempered messages; the list is empty if there is none.
     */
    public List<SubBlock<Integer, R>> getTemperedMessageIfAny (int seq_start, int seq_end) {

        List<SubBlock<Integer, R>> temperedMessage = new ArrayList<>();

        if(seq_start < seq_end)
            throw new IllegalArgumentException("The interval is not valid; " + seq_start + " appears before in the blockchain" +
                    "than " + seq_end);

        int partBeforeStart = howManyBlockchainPartBeforeReaching(seq_start);

        loadPreviousOrNextPartsByANumber(partBeforeStart);

        String hash = getLastBlockHash();
        boolean searchingTempMsgMode = false;
        boolean stop = false;

        while(!stop) {

            while(!hash.equals("0") && !stop) {
                Block<Integer, R> block = blockchain.get(hash);

                String hashBlocks = block.getLastSubBlockHash();
                int seqNumberBlocks = -1;

                while(!hashBlocks.equals("0") && (seqNumberBlocks != seq_end)) {
                    SubBlock<Integer, R> subBlock = block.getTransaction().get(hashBlocks);
                    seqNumberBlocks = subBlock.getMeta_Data();

                    if(seqNumberBlocks == seq_start) {
                        searchingTempMsgMode = true;
                    }

                    if(searchingTempMsgMode) {
                        if(!subBlock.isAuthentic() || !subBlock.equals(block.getTransaction().get(subBlock.getHashBlock()))) {
                            temperedMessage.add(subBlock);
                        }
                    }

                    hashBlocks = subBlock.getPreviousHashBlock();

                }

                if(seqNumberBlocks == seq_end)
                    stop = true;

                hash = block.getPreviousHashBlock();
            }

            if(!stop)
                try{
                    loadPreviousPartBlockchain();
                    hash = getLastBlockHash();
                } catch (Exception e) {
                    return null;
                }

        }

        restoreBlockchainFromJson(); //reset to the last part

        return temperedMessage;
    }

    /**
     * Checks if the message corresponding to seqNumber in the blockchain has been tempered.
     *
     * @param seqNumber the sequence number of the message to be checked
     *
     * @return the message corresponding to that subblock if it has been tempered; null if not or if it is not found.
     */
    public SubBlock<Integer, R> getTemperedMessageIfAny (int seqNumber) {

        List<SubBlock<Integer, R>> output = getTemperedMessageIfAny(seqNumber, seqNumber);

        if(output == null || output.isEmpty())
            return null;

        return output.get(0);
    }

    @Override
    public BlockchainIntSequence<R> jsonToBlockchain(int nBlockchain) {

        Gson gson =  new Gson();

        BlockchainIntSequence<R> blockchainFromJson = new BlockchainIntSequence<>("/", Long.MAX_VALUE);
        FileReader fileReader = null;

        String pathDirectory = this.path;

        pathDirectory += "/blockchain" + nBlockchain + ".json";

        try {
            fileReader = new FileReader(pathDirectory);

            blockchainFromJson = (BlockchainIntSequence<R>) gson.fromJson(fileReader, BlockchainIntSequence.class);

            fileReader.close();

        } catch(FileNotFoundException e){
            System.out.println("Errors trying to find the part " + nBlockchain + " of the Blockchain");
        }catch (IOException f){
            System.out.println("Error reading the file");
        }

        return blockchainFromJson;

    }
}
