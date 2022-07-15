package BlockchainImplementation.Blockchain;

import java.io.File;
import java.util.List;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;

/**
 * Interface for a Blockchain which stores two type of information: meta data of transaction, content of transaction.
 *
 * @param <T> type of the meta data of transaction
 * @param <R> type of the content of transaction
 */
public interface BlockchainInterface<T,R> {

    public void addABlock(T[] meta_data, R[] content);

    public String getLastBlockHash();

    public Block getBlockFromHash(String hashBlock);

    public List<SubBlock<T, R>> getTemperedMessageIfAny ();

    public List<SubBlock<T, R>> getTemperedMessageIfAny (String hashStart, String hashEnd);

    public List<SubBlock<T, R>> getTemperedMessageFromABlockIfAny (T[] meta_data, R[] content);

    public List<SubBlock<T, R>> getTemperedMessageFromABlockIfAny (File file);

    public List<SubBlock<T, R>> getTemperedMessageFromABlockIfAny (String path);

    public void loadPreviousPartBlockchain ();

    public void loadNextPartBlockchain ();

    public void blockchainToJson(long limitByteSize);

    public void blockchainToJson();

    public void restoreBlockchainFromJson();

    public void restoreBlockchainFromJson(int nBlockchain);

}
