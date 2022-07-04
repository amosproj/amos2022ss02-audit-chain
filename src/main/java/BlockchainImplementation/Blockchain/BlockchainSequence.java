package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;

import java.io.File;
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

        if(numberBlockchain == 1)
            return output;

        try {

            File file = new File(this.path + "/sequenceRecords.csv");

            Scanner input = new Scanner(file);

            while (!found) {
                String line = input.nextLine();

                String[] arrayCSV = line.split(",");

                if(arrayCSV[0].equals("NUMBER_FILE"))
                    break;

                output++;

                int left = Integer.parseInt(arrayCSV[1]);

                if(left <= seqNumber)
                    found = true;
            }

            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(!found)
            throw new IllegalArgumentException("Sequence Number has not been found in this and before this part of the blockchain");

        return output;
    }

    public List<SubBlock<Integer, R>> getTemperedMessageIfAny (int seq_start, int seq_end) {

        List<SubBlock<Integer, R>> temperedMessage = new ArrayList<>();

        if(seq_start < seq_end)
            throw new IllegalArgumentException("The interval is not valid; " + seq_start + " appears before in the blockchain" +
                    "than " + seq_end);

        int partBeforeStart = howManyBlockchainPartBeforeReaching(seq_start);
        int partBeforeEnd = howManyBlockchainPartBeforeReaching(seq_end);

        this.blockchainToJson(Long.MAX_VALUE);

        while(partBeforeStart > 0) {
            loadPreviousPartBlockchain();
            partBeforeStart--;
            partBeforeEnd--;
        }

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
}
