package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

/**
 * This class is supposed to be an interface for a generic user / client, which does not know anything about the
 * internal structure of the blockchain. So it also has methods that directly transform results in json.
 *
 * @param <R> the type of the content of the transactions
 */
public class BlockchainIntSequenceAPI<R> extends BlockchainIntSequence<R>{

    public BlockchainIntSequenceAPI(String pathDirectory, long maxSizeByte) {
        super(pathDirectory, maxSizeByte);
    }

    private String resultListToJson (List<SubBlock<Integer, R>> result) {
        Gson gson = new Gson();

        return gson.toJson(result);
    }

    private String resultToJson (SubBlock<Integer, R> result) {
        Gson gson = new Gson();

        return gson.toJson(result);
    }

    public String getTemperedMessageIfAnyAsString () {
        return resultListToJson(getTemperedMessageIfAny());
    }

    public String getTemperedMessageIfAnyAsString (String hashStart, String hashEnd) {
        return resultListToJson(getTemperedMessageIfAny(hashStart, hashEnd));
    }

    public String getTemperedMessageFromABlockIfAnyAsString (Integer[] meta_data, R[] content) {
        return resultListToJson(getTemperedMessageFromABlockIfAny(meta_data, content));
    }

    public String getTemperedMessageFromABlockIfAnyAsString (File file) {
        return resultListToJson(getTemperedMessageFromABlockIfAny(file));
    }

    public String getTemperedMessageFromABlockIfAnyAsString (String path) {
        return resultListToJson(getTemperedMessageFromABlockIfAny(path));
    }

    public String getTemperedMessageIfAnyAsString (int seq_start, int seq_end) {
        return resultListToJson(getTemperedMessageIfAny(seq_start, seq_end));
    }

    public String getTemperedMessageIfAnyAsString (int seqNumber) {
        return resultToJson(getTemperedMessageIfAny(seqNumber));
    }


}
