package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class BlockchainTest {

    @AfterAll
    public static void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get("the-file-name.json"));
    }

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
    public void getTemperedMessageIfAnyOverATemperedBlockchainShouldReturnListWithSubBlocksTempered() {
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

    @Test
    @DisplayName("getTemperedMessageIfAny() over a tempered blockchain in which a Block has had a non-authorized addition should return the list of its subBlocks")
    public void getTemperedMessageIfAnyOverATemperedBlockchainInWhichABlockHadUnauthorizedAdditionShouldReturnAListWithItsSubblocks() {
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                new String[]{"g", "h", "i"});

        Block<String, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        SubBlock<String, String> subBlock = new SubBlock<>(last.getTransaction().get(last.getLastSubBlockHash()).getHashBlock(),
                                                        "10", "l");
        transactions.put(subBlock.getHashBlock(), subBlock);

        List<SubBlock<String, String>> temperedTransaction = blockchain.getTemperedMessageIfAny();
        List<SubBlock<String, String>> temperedTransaction2 = new ArrayList<>();

        temperedTransaction2.addAll(last.getTransaction().values());

        assertThat(temperedTransaction).isEqualTo(temperedTransaction2);

    }

    @Test
    @DisplayName("blockchainToJson() json comparison should return true")
    public void blockchainToJsonShouldReturnTrue() throws IOException {
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                             new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                             new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                             new String[]{"g", "h", "i"});

        blockchain.blockchainToJson();
        String fileAlist = Files.readString(Paths.get("the-file-name.json"));

        String fileString = fileAlist.toString();
        String blockchainString = "{\"blockchain\":{\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\":{\"lastSubBlockHash\":\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\",\"previousHashBlock\":\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\",\"hashBlock\":\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\",\"transaction\":{\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\":{\"meta_data\":\"6\",\"previousHashBlock\":\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\",\"hashBlock\":\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\",\"transaction\":\"f\"},\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\":{\"meta_data\":\"5\",\"previousHashBlock\":\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\",\"hashBlock\":\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\",\"transaction\":\"e\"},\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\":{\"meta_data\":\"4\",\"previousHashBlock\":\"0\",\"hashBlock\":\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\",\"transaction\":\"d\"}}},\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\":{\"lastSubBlockHash\":\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\",\"previousHashBlock\":\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\",\"hashBlock\":\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\",\"transaction\":{\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\":{\"meta_data\":\"8\",\"previousHashBlock\":\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\",\"hashBlock\":\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\",\"transaction\":\"h\"},\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\":{\"meta_data\":\"9\",\"previousHashBlock\":\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\",\"hashBlock\":\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\",\"transaction\":\"i\"},\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\":{\"meta_data\":\"7\",\"previousHashBlock\":\"0\",\"hashBlock\":\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\",\"transaction\":\"g\"}}},\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\":{\"lastSubBlockHash\":\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\",\"previousHashBlock\":\"0\",\"hashBlock\":\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\",\"transaction\":{\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\":{\"meta_data\":\"2\",\"previousHashBlock\":\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\",\"hashBlock\":\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\",\"transaction\":\"b\"},\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\":{\"meta_data\":\"1\",\"previousHashBlock\":\"0\",\"hashBlock\":\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\",\"transaction\":\"a\"},\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\":{\"meta_data\":\"3\",\"previousHashBlock\":\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\",\"hashBlock\":\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\",\"transaction\":\"c\"}}}},\"lastBlockHash\":\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\"}";

        assertThat(fileString).isEqualTo(blockchainString);
    }

    @Test
    @DisplayName("jsonToBlockchain() blockchain object comparison should return true")
    public void jsonToBlockchainShouldReturnTrue() {
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                new String[]{"g", "h", "i"});
        blockchain.blockchainToJson();

        Blockchain<String, String> blockchain2 = new Blockchain<>();

        blockchain2.jsonToBlockchain(Paths.get("the-file-name.json"));assertThat(blockchain).isEqualTo(blockchain2);
    }

}
