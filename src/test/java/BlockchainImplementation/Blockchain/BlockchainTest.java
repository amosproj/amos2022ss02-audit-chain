package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class BlockchainTest {


    @Test
    @DisplayName("A new Blockchain should have as its last block '0'")
    public void aNewBlockchainMustHaveItsLatBlockAs0() {
        Blockchain<String, String> blockchain = new Blockchain<>();

        assertThat(blockchain.getLastBlockHash()).isEqualTo("0");
    }



    @Test
    @DisplayName("A Blockchain with a block should not have as its last block '0'")
    public void aBlockchainWithABlockShouldNotHaveItsLastBlockAs0() {
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                            new String[]{"a", "b", "c"});

        assertThat(blockchain.getLastBlockHash()).isNotEqualTo("0");
    }

    @Test
    @DisplayName("Hash of the blocks in the blockchain should allow to find them starting from the last block hash")
    public void hashBlocksInBlockchainShouldAllowToFindThemStartingFromLastBlockHash() {
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                             new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                             new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                             new String[]{"g", "h", "i"});

        String lastHashBlock = blockchain.getLastBlockHash();
        Block<String, String> third = blockchain.getBlockFromHash(lastHashBlock);
        Block<String, String> second = blockchain.getBlockFromHash(third.getPreviousHashBlock());

        Block<String, String> dumFirst = new Block<>("0", new String[]{"1", "2", "3"},
                                                                          new String[]{"a", "b", "c"});

        Block<String, String> dumSecond = new Block<>(dumFirst.getHashBlock(), new String[]{"4", "5", "6"},
                                                                                new String[]{"d", "e", "f"});

        assertThat(second.getHashBlock()).isEqualTo(dumSecond.getHashBlock());
    }

    @Test
    @DisplayName("getTemperedMessageIfAny() over a unchanged blockchain should return an empty List")
    public void getTemperedMessageIfAnyOverAUnchangedBlockchainShouldReturnAnEmptyList() {
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                new String[]{"g", "h", "i"});

        assertThat(blockchain.getTemperedMessageIfAny().size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedMessageIfAny() over a tempered blockchain should return the List with subBlocks tempered")
    public void getTemperedMessageIfAnyOverATemperedBlockchainShouldReturnFalse() {
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                             new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                           new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                            new String[]{"g", "h", "i"});

        Block<String, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put("0000000", last.getTransaction().get(last.getLastSubBlockHash()));

        List<SubBlock<String, String>> temperedTransaction = blockchain.getTemperedMessageIfAny();

        assertThat(temperedTransaction.get(0)).isEqualTo(last.getTransaction().get(last.getLastSubBlockHash()));

    }

}
