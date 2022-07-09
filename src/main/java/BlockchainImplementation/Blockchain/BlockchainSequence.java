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

public class BlockchainSequence<R> extends Blockchain<Integer, R>{


    public BlockchainSequence(String pathDirectory, long maxSizeByte) {
        super(pathDirectory, maxSizeByte);
    }

    private int howManyBlockchainPartBeforeReaching (int seqNumber) throws IllegalArgumentException {

        int output = 0;
        boolean found = false;
        String[] arrayCSV;

        if(numberBlockchain == 1)
            return output;

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
            ex.printStackTrace();
        }

        if(!found) {
            jsonToBlockchain();
            Block<Integer, R> lastB = blockchain.get(getLastBlockHash());
            SubBlock<Integer, R> lastSub = lastB.getTransaction().get(lastB.getLastSubBlockHash());
            output = this.numberBlockchain;

            System.out.println(lastSub.getMeta_Data());

            int lastSeqNumber = lastSub.getMeta_Data();


            if(seqNumber > lastSeqNumber)
                throw new IllegalArgumentException("Sequence Number has not been found in this and before this part of the blockchain");
        }

        System.out.println("Num of this bc: " + this.numberBlockchain);
        System.out.println(output);

        return this.numberBlockchain - output;
    }
//
//    private List<SubBlock<Integer, R>> getTemperedMessageIfAny

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

    public List<SubBlock<Integer, R>> getTemperedMessageIfAny (int seq_start, int seq_end) {

        List<SubBlock<Integer, R>> temperedMessage = new ArrayList<>();

        if(seq_start < seq_end)
            throw new IllegalArgumentException("The interval is not valid; " + seq_start + " appears before in the blockchain" +
                    "than " + seq_end);

//        this.blockchainToJson(Long.MAX_VALUE); I think everything is saved after each adding a block

        System.out.println(seq_start);
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

                    if(seqNumberBlocks == seq_start)
                        searchingTempMsgMode = true;

                    if(searchingTempMsgMode) {
                        if(!subBlock.isAuthentic() || !subBlock.equals(block.getTransaction().get(subBlock.getHashBlock())))
                            temperedMessage.add(subBlock);
                    }

                    hashBlocks = subBlock.getPreviousHashBlock();
                    if(seqNumberBlocks == seq_end)
                        stop = true;
                }

                hash = block.getPreviousHashBlock();
            }

            if(!stop)
                try{
                    loadPreviousPartBlockchain();
                } catch (Exception e) {
                    return null;
                }

        }

        jsonToBlockchain(); //reset to the last part

        return temperedMessage;
    }

    @Override
    public BlockchainSequence<R> jsonToBlockchain(int nBlockchain) {

        Gson gson =  new Gson();

        BlockchainSequence<R> blockchainFromJson = new BlockchainSequence<>("/", Long.MAX_VALUE);
        FileReader fileReader = null;

        String pathDirectory = this.path;

        pathDirectory += "/blockchain" + nBlockchain + ".json";

        try {
            fileReader = new FileReader(pathDirectory);

            blockchainFromJson = (BlockchainSequence<R>) gson.fromJson(fileReader, BlockchainSequence.class);

            fileReader.close();

        } catch(FileNotFoundException e){
            System.out.println("Errors trying to find the part " + nBlockchain + " of the Blockchain");
        }catch (IOException f){
            System.out.println("Error reading the file");
        }

        return blockchainFromJson;

    }
}
