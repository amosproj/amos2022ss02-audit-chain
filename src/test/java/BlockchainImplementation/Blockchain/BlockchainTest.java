package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import BlockchainImplementation.Blockchain.Hashing.Hasher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BlockchainTest {


    @Test
    @DisplayName("A new Blockchain should have as its last block '0'")
    public void aNewBlockchainMustHaveItsLatBlockAs0() {
        Blockchain<String, String> blockchain = new Blockchain<>();

        assertThat(blockchain.getLastBlockHash()).isEqualTo("0");
    }

//    @Test
//    @DisplayName("Adding a block to an empty Blockchain should be possible")
//    public void addingABlockToAnEmptyBlockchainShouldWork() throws IOException {
//        Blockchain<String, String> blockchain = new Blockchain<>();
//        blockchain.addABlock(new String[]{"1", "2", "3"},
//                            new String[]{"a", "b", "c"});
//
//        assertThat(blockchain.getLastBlockHash()).isNotEqualTo("0");
//    }
//
////    Block block = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
////    Map<String, SubBlock> map = (Map<String, SubBlock>) block.getTransaction();
////    SubBlock<String,String> secondary = new SubBlock<>("0", );
////    SubBlock<String, String> subBlock = map.get(Hasher.hashSHA256())
////
////    assertAll(
////                () -> assertThat(map.get("1")).isEqualTo("a"),
////                () -> assertThat(map.get("2")).isEqualTo("b"),
////                () -> assertThat(map.get("3")).isEqualTo("c"));
}
