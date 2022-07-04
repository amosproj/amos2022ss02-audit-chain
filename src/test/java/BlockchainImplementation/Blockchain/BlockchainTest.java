package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BlockchainTest {

    private static Blockchain<String, String> setupBlockchain() {

        Blockchain<String, String> blockchain = new Blockchain<>("src/test/resources/testOutput/", Long.MAX_VALUE);
        blockchain.addABlock(new String[]{"1", "2", "3"},
                new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                new String[]{"g", "h", "i"});

        return blockchain;

    }

    @AfterEach
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/blockchain1.json"));
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/blockchain2.json"));
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/lastBlockchain.txt"));
        Files.deleteIfExists(Paths.get("test.txt"));
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/sequenceRecords.csv"));
    }

    @Test
    @DisplayName("A new Blockchain should have as its last block '0'")
    public void aNewBlockchainMustHaveItsLatBlockAs0() {
        Blockchain<String, String> blockchain = new Blockchain<>("src/test/resources/testOutput/", Long.MAX_VALUE);

        assertThat(blockchain.getLastBlockHash()).isEqualTo("0");
    }


    @Test
    @DisplayName("A Blockchain with a block should not have as its last block '0'")
    public void aBlockchainWithABlockShouldNotHaveItsLastBlockAs0() {
        Blockchain<String, String> blockchain = new Blockchain<>("src/test/resources/testOutput/", Long.MAX_VALUE);
        blockchain.addABlock(new String[]{"1", "2", "3"},
                            new String[]{"a", "b", "c"});

        assertThat(blockchain.getLastBlockHash()).isNotEqualTo("0");
    }

    @Test
    @DisplayName("Hash of the blocks in the blockchain should allow to find them starting from the last block hash")
    public void hashBlocksInBlockchainShouldAllowToFindThemStartingFromLastBlockHash() {

        Blockchain<String, String> blockchain = setupBlockchain();

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

        Blockchain<String, String> blockchain = setupBlockchain();

        assertThat(blockchain.getTemperedMessageIfAny().size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedMessageIfAny() putting a Illegal Interval should throw IllegalArgumentException")
    public void getTemperedMessageIfAnyPuttingAIllegalIntervalShouldThrowIllegalArgumentException() {

        Blockchain<String, String> blockchain = setupBlockchain();

        assertThrows(IllegalArgumentException.class, () -> blockchain.getTemperedMessageIfAny("0",blockchain.getLastBlockHash()));

    }

    @Test
    @DisplayName("getTemperedMessageIfAny() over a tempered blockchain should return the List with subBlocks tempered")
    public void getTemperedMessageIfAnyOverATemperedBlockchainShouldReturnListWithSubBlocksTempered() {
        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put("0000000", last.getTransaction().get(last.getLastSubBlockHash()));

        List<SubBlock<String, String>> temperedTransaction = blockchain.getTemperedMessageIfAny();

        assertThat(temperedTransaction.get(0)).isEqualTo(last.getTransaction().get(last.getLastSubBlockHash()));

    }

    @Test
    @DisplayName("getTemperedMessageIfAny() over a tempered blockchain in which a Block has had a non-authorized addition should return the list of its subBlocks")
    public void getTemperedMessageIfAnyOverATemperedBlockchainInWhichABlockHadUnauthorizedAdditionShouldReturnAListWithItsSubblocks() {
        Blockchain<String, String> blockchain = setupBlockchain();

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
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a block not in the blockchain should return null ")
    public void getTemperedMessageFromABlockIfAnyPassingABlockNotInTheBlockchainShouldReturnNull() {

        Blockchain<String, String> blockchain = setupBlockchain();

        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new String[]{"0", "2", "3"},new String[]{"a", "b", "c"});

        assertThat(temperedTransaction).isNull();

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a non tempered block in a tempered (other blocks) blockchain should return empty list ")
    public void getTemperedMessageFromABlockIfAnyPassingANonTemperedBlockInTemperedBlockchainShouldReturnEmptyList() {

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "9", "i"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new String[]{"1", "2", "3"},new String[]{"a", "b", "c"});

        assertThat(temperedTransaction.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a non tempered block in a tempered (previous blocks) blockchain should return empty list ")
    public void getTemperedMessageFromABlockIfAnyPassingANonTemperedBlockInTemperedBlockchainShouldReturnEmptyList2() {

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                                    blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "6", "f"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new String[]{"7", "8", "9"},new String[]{"g", "h", "i"});

        assertThat(temperedTransaction.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a tempered block in a tempered blockchain should return tempered SubBlock ")
    public void getTemperedMessageFromABlockIfAnyPassingATemperedBlockInTemperedBlockchainShouldReturnTemperedSubBlock() {

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "6", "f"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new String[]{"4", "5", "6"},new String[]{"d", "e", "f"});

        SubBlock<String, String> supposedResult = new SubBlock<>("dasdasd", "6", "f");

        assertAll (
            () -> assertThat(temperedTransaction.get(0)).isEqualTo(supposedResult),
            () -> assertThat(temperedTransaction.size()).isEqualTo(1)
        );

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a complete tempered block in a tempered blockchain should return all its SubBlock ")
    public void getTemperedMessageFromABlockIfAnyPassingACompleteTemperedBlockInTemperedBlockchainShouldReturnAllItsSubBlock() {

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        SubBlock<String, String> unauthorized = new SubBlock<>(last.getLastSubBlockHash(), "7", "g");
        transactions.put(unauthorized.getHashBlock(), unauthorized);


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new String[]{"4", "5", "6"},new String[]{"d", "e", "f"});

        List<SubBlock<String, String>> supposedResult = new ArrayList<>();

        for(String key : transactions.keySet())
            supposedResult.add(transactions.get(key));

        assertAll (
                () -> assertThat(temperedTransaction).isEqualTo(supposedResult),
                () -> assertThat(temperedTransaction.size()).isEqualTo(4)
        );

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a block from a File not in the blockchain should return null ")
    public void getTemperedMessageFromABlockIfAnyPassingABlockFromAFileNotInTheBlockchainShouldReturnNull() throws IOException {

        Files.writeString(Path.of("test.txt"), "0\na\nuseless\n2\nb\nuseless\n3\nc\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new File("test.txt"));

        assertThat(temperedTransaction).isNull();

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a non tempered block from a File in a tempered (other blocks) blockchain should return empty list ")
    public void getTemperedMessageFromABlockIfAnyPassingANonTemperedBlockFromAFileInTemperedBlockchainShouldReturnEmptyList() throws IOException {

        Files.writeString(Path.of("test.txt"), "1\na\nuseless\n2\nb\nuseless\n3\nc\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "9", "i"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new File("test.txt"));

        assertThat(temperedTransaction.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a non tempered block from a File in a tempered (previous blocks) blockchain should return empty list ")
    public void getTemperedMessageFromABlockIfAnyPassingANonTemperedBlockFromAFileInTemperedBlockchainShouldReturnEmptyList2() throws IOException {

        Files.writeString(Path.of("test.txt"), "7\ng\nuseless\n8\nh\nuseless\n9\ni\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "6", "f"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new File("test.txt"));

        assertThat(temperedTransaction.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a tempered block from a File in a tempered blockchain should return tempered SubBlock ")
    public void getTemperedMessageFromABlockIfAnyPassingATemperedBlockFromAFileInTemperedBlockchainShouldReturnTemperedSubBlock() throws IOException {

        Files.writeString(Path.of("test.txt"), "4\nd\nuseless\n5\ne\nuseless\n6\nf\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "6", "f"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new File("test.txt"));

        SubBlock<String, String> supposedResult = new SubBlock<>("dasdasd", "6", "f");

        assertAll (
                () -> assertThat(temperedTransaction.get(0)).isEqualTo(supposedResult),
                () -> assertThat(temperedTransaction.size()).isEqualTo(1)
        );

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a complete tempered block from a File in a tempered blockchain should return all its SubBlock ")
    public void getTemperedMessageFromABlockIfAnyPassingACompleteTemperedBlockFromAFileInTemperedBlockchainShouldReturnAllItsSubBlock() throws IOException {

        Files.writeString(Path.of("test.txt"), "4\nd\nuseless\n5\ne\nuseless\n6\nf\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        SubBlock<String, String> unauthorized = new SubBlock<>(last.getLastSubBlockHash(), "7", "g");
        transactions.put(unauthorized.getHashBlock(), unauthorized);


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new File("test.txt"));

        List<SubBlock<String, String>> supposedResult = new ArrayList<>();

        for(String key : transactions.keySet())
            supposedResult.add(transactions.get(key));

        assertAll (
                () -> assertThat(temperedTransaction).isEqualTo(supposedResult),
                () -> assertThat(temperedTransaction.size()).isEqualTo(4)
        );

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a block from a File Path not in the blockchain should return null ")
    public void getTemperedMessageFromABlockIfAnyPassingABlockFromAFilePathNotInTheBlockchainShouldReturnNull() throws IOException {

        Files.writeString(Path.of("test.txt"), "0\na\nuseless\n2\nb\nuseless\n3\nc\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny("test.txt");

        assertThat(temperedTransaction).isNull();

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a non tempered block from a File Path in a tempered (other blocks) blockchain should return empty list ")
    public void getTemperedMessageFromABlockIfAnyPassingANonTemperedBlockFromAFilePathInTemperedBlockchainShouldReturnEmptyList() throws IOException {

        Files.writeString(Path.of("test.txt"), "1\na\nuseless\n2\nb\nuseless\n3\nc\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "9", "i"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny("test.txt");

        assertThat(temperedTransaction.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a non tempered block from a File Path in a tempered (previous blocks) blockchain should return empty list ")
    public void getTemperedMessageFromABlockIfAnyPassingANonTemperedBlockFromAFilePathInTemperedBlockchainShouldReturnEmptyList2() throws IOException {

        Files.writeString(Path.of("test.txt"), "7\ng\nuseless\n8\nh\nuseless\n9\ni\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "6", "f"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny("test.txt");

        assertThat(temperedTransaction.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a tempered block from a File Path in a tempered blockchain should return tempered SubBlock ")
    public void getTemperedMessageFromABlockIfAnyPassingATemperedBlockFromAFilePathInTemperedBlockchainShouldReturnTemperedSubBlock() throws IOException {

        Files.writeString(Path.of("test.txt"), "4\nd\nuseless\n5\ne\nuseless\n6\nf\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "6", "f"));


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny("test.txt");

        SubBlock<String, String> supposedResult = new SubBlock<>("dasdasd", "6", "f");

        assertAll (
                () -> assertThat(temperedTransaction.get(0)).isEqualTo(supposedResult),
                () -> assertThat(temperedTransaction.size()).isEqualTo(1)
        );

    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a complete tempered block from a File Path in a tempered blockchain should return all its SubBlock ")
    public void getTemperedMessageFromABlockIfAnyPassingACompleteTemperedBlockFromAFilePathInTemperedBlockchainShouldReturnAllItsSubBlock() throws IOException {

        Files.writeString(Path.of("test.txt"), "4\nd\nuseless\n5\ne\nuseless\n6\nf\nuseless");

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        SubBlock<String, String> unauthorized = new SubBlock<>(last.getLastSubBlockHash(), "7", "g");
        transactions.put(unauthorized.getHashBlock(), unauthorized);


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny("test.txt");

        List<SubBlock<String, String>> supposedResult = new ArrayList<>();

        for(String key : transactions.keySet())
            supposedResult.add(transactions.get(key));

        assertAll (
                () -> assertThat(temperedTransaction).isEqualTo(supposedResult),
                () -> assertThat(temperedTransaction.size()).isEqualTo(4)
        );

    }


    @Test
    @DisplayName("blockchainToJson() should save in a file the json of the blockchain and put 1 as last number of blockchain")
    public void blockchainToJsonShouldSaveinAFileTheJsonOfTheBlockchainAndPut1AsLastNumberOfBlockchain() throws IOException {
        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson(Long.MAX_VALUE);
        String fileAlist = Files.readString(Paths.get("src/test/resources/testOutput/blockchain1.json"));

        String blockchainString = "{\"numberBlockchain\":1,\"blockchain\":{\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\":{\"firstSubBlockHash\":\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\",\"lastSubBlockHash\":\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\",\"previousHashBlock\":\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\",\"hashBlock\":\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\",\"transaction\":{\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\":{\"meta_data\":\"6\",\"previousHashBlock\":\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\",\"hashBlock\":\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\",\"transaction\":\"f\"},\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\":{\"meta_data\":\"5\",\"previousHashBlock\":\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\",\"hashBlock\":\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\",\"transaction\":\"e\"},\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\":{\"meta_data\":\"4\",\"previousHashBlock\":\"0\",\"hashBlock\":\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\",\"transaction\":\"d\"}}},\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\":{\"firstSubBlockHash\":\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\",\"lastSubBlockHash\":\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\",\"previousHashBlock\":\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\",\"hashBlock\":\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\",\"transaction\":{\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\":{\"meta_data\":\"8\",\"previousHashBlock\":\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\",\"hashBlock\":\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\",\"transaction\":\"h\"},\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\":{\"meta_data\":\"9\",\"previousHashBlock\":\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\",\"hashBlock\":\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\",\"transaction\":\"i\"},\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\":{\"meta_data\":\"7\",\"previousHashBlock\":\"0\",\"hashBlock\":\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\",\"transaction\":\"g\"}}},\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\":{\"firstSubBlockHash\":\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\",\"lastSubBlockHash\":\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\",\"previousHashBlock\":\"0\",\"hashBlock\":\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\",\"transaction\":{\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\":{\"meta_data\":\"2\",\"previousHashBlock\":\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\",\"hashBlock\":\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\",\"transaction\":\"b\"},\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\":{\"meta_data\":\"1\",\"previousHashBlock\":\"0\",\"hashBlock\":\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\",\"transaction\":\"a\"},\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\":{\"meta_data\":\"3\",\"previousHashBlock\":\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\",\"hashBlock\":\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\",\"transaction\":\"c\"}}}},\"lastBlockHash\":\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\",\"locked\":false,\"path\":\"src/test/resources/testOutput/\",\"maxByte\":9223372036854775807}";
        String nBlockchain = Files.readString(Paths.get("src/test/resources/testOutput/lastBlockchain.txt"));
        int iNBlockchain = Integer.parseInt(nBlockchain);

        assertAll(
                () -> assertThat(fileAlist).isEqualTo(blockchainString),
                () -> assertThat(iNBlockchain).isEqualTo(1)
        );

    }

    @Test
    @DisplayName("jsonToBlockchain() should take back the right blockchain")
    public void jsonToBlockchainShouldTakeBackTheRightBlockchain() {
        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson(Long.MAX_VALUE);

        Blockchain<String, String> blockchain2 = new Blockchain<>("src/test/resources/testOutput/", Long.MAX_VALUE);

        blockchain2.jsonToBlockchain();

        assertThat(blockchain2).isEqualTo(blockchain);
    }

    @Test
    @DisplayName("blockchainToJson() should save in two files a blockchain that exceeds in size")
    public void blockchainToJsonShouldSaveInTwoFilesABlockchainThatExceedsInSize() throws IOException {
        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( Long.MAX_VALUE);


        String fileAlist = Files.readString(Paths.get("src/test/resources/testOutput/blockchain1.json"));
        String fileAlist2 = Files.readString(Paths.get("src/test/resources/testOutput/blockchain2.json"));

        String blockchainString = "{\"numberBlockchain\":1,\"blockchain\":{\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\":{\"firstSubBlockHash\":\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\",\"lastSubBlockHash\":\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\",\"previousHashBlock\":\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\",\"hashBlock\":\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\",\"transaction\":{\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\":{\"meta_data\":\"6\",\"previousHashBlock\":\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\",\"hashBlock\":\"1274709047133e3e49d841f9f03324646df9cc0ffbc0437d345d556434e6e36d\",\"transaction\":\"f\"},\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\":{\"meta_data\":\"5\",\"previousHashBlock\":\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\",\"hashBlock\":\"36a0b872fbe8131912374546862e72f40fce75efba29010854851c23a22de654\",\"transaction\":\"e\"},\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\":{\"meta_data\":\"4\",\"previousHashBlock\":\"0\",\"hashBlock\":\"4322a200078aa064d7b95a0f689c775d1ec7c9dd9cb71133a1b54ad344d47d84\",\"transaction\":\"d\"}}},\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\":{\"firstSubBlockHash\":\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\",\"lastSubBlockHash\":\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\",\"previousHashBlock\":\"4b84df364bacbe8f6bb756ae85607051950121fb1c257a35e74d254f2e37051a\",\"hashBlock\":\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\",\"transaction\":{\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\":{\"meta_data\":\"8\",\"previousHashBlock\":\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\",\"hashBlock\":\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\",\"transaction\":\"h\"},\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\":{\"meta_data\":\"9\",\"previousHashBlock\":\"40781e6fb5728e3eaab753d2ce0841182690062f305499663aae8299571f7b42\",\"hashBlock\":\"629897848ac7c45379bc8a0ed0bc2c9aec3a2392a630a929d910fb7f0a44b2f1\",\"transaction\":\"i\"},\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\":{\"meta_data\":\"7\",\"previousHashBlock\":\"0\",\"hashBlock\":\"ba71216589005be8e99cb6027e3fcf3e7855577fc6f35d8559853b97329b9c29\",\"transaction\":\"g\"}}},\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\":{\"firstSubBlockHash\":\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\",\"lastSubBlockHash\":\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\",\"previousHashBlock\":\"0\",\"hashBlock\":\"02f25b9d7235c4bd3745a1292c30f10c57abd802b4fe323a9e0b3e1d20bb3b30\",\"transaction\":{\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\":{\"meta_data\":\"2\",\"previousHashBlock\":\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\",\"hashBlock\":\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\",\"transaction\":\"b\"},\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\":{\"meta_data\":\"1\",\"previousHashBlock\":\"0\",\"hashBlock\":\"8b1bf74ad28fc10cce5cc14966f38e4ffd0a493385c9855ce4d5677b7157a6fb\",\"transaction\":\"a\"},\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\":{\"meta_data\":\"3\",\"previousHashBlock\":\"7e9126291e5726457a38b262760c0b58624977f2798029e8e5550bf1bdbcb12c\",\"hashBlock\":\"41c445c80c4dc74660a0fb413010aa0432ecf24d9415403813f6485c21f9ff63\",\"transaction\":\"c\"}}}},\"lastBlockHash\":\"b1eeb2de18a4520f43875bc81d8e1b41d211c85bbd2aaef04348dd370f05813a\",\"locked\":true,\"path\":\"src/test/resources/testOutput/\",\"maxByte\":9223372036854775807}";
        String blockchainString2 = "{\"numberBlockchain\":2,\"blockchain\":{\"f7a8936471a53c5222de3a331fcdeaca7fb6e96065a1f2f64d83739df9616070\":{\"firstSubBlockHash\":\"acfa5879c368891ccc18961fcf6a24b80cc96ad0708bafdbb2b6470dc2e76b18\",\"lastSubBlockHash\":\"bf70918e2c16e83dc7668333b89ba9a5c1f60ea8207d84bd19e902e4bb510327\",\"previousHashBlock\":\"0\",\"hashBlock\":\"f7a8936471a53c5222de3a331fcdeaca7fb6e96065a1f2f64d83739df9616070\",\"transaction\":{\"acfa5879c368891ccc18961fcf6a24b80cc96ad0708bafdbb2b6470dc2e76b18\":{\"meta_data\":\"10\",\"previousHashBlock\":\"0\",\"hashBlock\":\"acfa5879c368891ccc18961fcf6a24b80cc96ad0708bafdbb2b6470dc2e76b18\",\"transaction\":\"l\"},\"a6e0e166f4dda8a6ab16d6ca45b8486c8e436c5b3e8ed2430742864ef047d11c\":{\"meta_data\":\"11\",\"previousHashBlock\":\"acfa5879c368891ccc18961fcf6a24b80cc96ad0708bafdbb2b6470dc2e76b18\",\"hashBlock\":\"a6e0e166f4dda8a6ab16d6ca45b8486c8e436c5b3e8ed2430742864ef047d11c\",\"transaction\":\"m\"},\"bf70918e2c16e83dc7668333b89ba9a5c1f60ea8207d84bd19e902e4bb510327\":{\"meta_data\":\"12\",\"previousHashBlock\":\"a6e0e166f4dda8a6ab16d6ca45b8486c8e436c5b3e8ed2430742864ef047d11c\",\"hashBlock\":\"bf70918e2c16e83dc7668333b89ba9a5c1f60ea8207d84bd19e902e4bb510327\",\"transaction\":\"n\"}}}},\"lastBlockHash\":\"f7a8936471a53c5222de3a331fcdeaca7fb6e96065a1f2f64d83739df9616070\",\"locked\":false,\"path\":\"src/test/resources/testOutput/\",\"maxByte\":9223372036854775807}";

        assertAll(
                () -> assertThat(fileAlist).isEqualTo(blockchainString),
                () -> assertThat(fileAlist2).isEqualTo(blockchainString2)
        );

    }

    @Test
    @DisplayName("jsonToBlockchain() in a multiple file blockchain should load just the last part")
    public void jsonToBlockchainInAMultipleFileBlockchainShouldLoadJustTheLastPart() {

        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson(1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( Long.MAX_VALUE);


        Blockchain<String, String> blockchain2 = new Blockchain<>("src/test/resources/testOutput/", Long.MAX_VALUE);

        blockchain2.jsonToBlockchain();

        assertThat(blockchain2).isEqualTo(blockchain);
    }

    @Test
    @DisplayName("loadPreviousPartBlockchain() in a multiple file blockchain from the second should load the first part")
    public void loadPreviousPartBlockchainInAMultipleFileBlockchainFromSecondShouldLoadFirstPart() throws IOException {

        Blockchain<String, String> blockchain2 = setupBlockchain();

        cleanUp();

        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson(1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson(Long.MAX_VALUE);

        blockchain.loadPreviousPartBlockchain();

        assertThat(blockchain).isEqualTo(blockchain2);
    }


    @Test
    @DisplayName("loadPreviousPartBlockchain() in a multiple file blockchain from the first part should throw error")
    public void loadPreviousPartBlockchainInAMultipleFileBlockchainFromFirstPartShouldThrowError() {

        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson(1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson(Long.MAX_VALUE);

        blockchain.loadPreviousPartBlockchain();

        assertThrows(RuntimeException.class, () -> blockchain.loadPreviousPartBlockchain());

    }

    @Test
    @DisplayName("loadNextPartBlockchain() in a multiple file blockchain from the first should load the second part")
    public void loadNextPartBlockchainInAMultipleFileBlockchainFromFirstShouldLoadSecondPart() throws IOException {

        Blockchain<String, String> blockchain2 = new Blockchain<>("src/test/resources/testOutput/", Long.MAX_VALUE);
        blockchain2.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        cleanUp();

        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson(1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( Long.MAX_VALUE);

        blockchain.loadPreviousPartBlockchain();

        blockchain.loadNextPartBlockchain();

        assertThat(blockchain).isEqualTo(blockchain2);
    }


    @Test
    @DisplayName("loadNextPartBlockchain() in a multiple file blockchain from the second part should throw error")
    public void loadNextPartBlockchainInAMultipleFileBlockchainFromSecondPartShouldThrowError() {

        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( Long.MAX_VALUE);

        assertThrows(RuntimeException.class, () -> blockchain.loadNextPartBlockchain());

    }

    @Test
    @DisplayName("trying to addABlock() to a previous part of the blockchain should throw error")
    public void tryingtoAddABlockToAPreviousPartOfTheBlockchainShouldThrowError() {

        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( Long.MAX_VALUE);

        blockchain.loadPreviousPartBlockchain();

        assertThrows(RuntimeException.class, () -> blockchain.addABlock(new String[]{"13", "14", "15"},
                                            new String[]{"o", "p", "q"}));

    }

    @Test
    @DisplayName("trying to addABlock() to the latest part of the blockchain should be possible")
    public void tryingtoAddABlockToTheLatestPartOfTheBlockchainShouldBePossible() {

        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson(1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( Long.MAX_VALUE);

        assertDoesNotThrow(() -> blockchain.addABlock(new String[]{"13", "14", "15"},
                                                      new String[]{"o", "p", "q"}));

    }


    @Test
    @DisplayName("getTemperedMessageIfAny() on a multi-file Blockchain should give back tempered messages")
    public void getTemperedMessageIfAnyOnAMultiFileBlockchainShouldGiveBackTemperedMessages() {

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put("0000000", last.getTransaction().get(last.getLastSubBlockHash()));

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( Long.MAX_VALUE);

        List<SubBlock<String, String>> temperedTransaction = blockchain.getTemperedMessageIfAny();

        assertThat(temperedTransaction.get(0)).isEqualTo(last.getTransaction().get(last.getLastSubBlockHash()));
    }

    @Test
    @DisplayName("getTemperedMessageIfAny() on a multi-file Blockchain but with temp message in last file should give back tempered messages")
    public void getTemperedMessageIfAnyOnAMultiFileBlockchainWithTempMessageInLastFileShouldGiveBackTemperedMessages() {

        Blockchain<String, String> blockchain = setupBlockchain();

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        Block<String, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put("0000000", last.getTransaction().get(last.getLastSubBlockHash()));

        blockchain.blockchainToJson( Long.MAX_VALUE);

        List<SubBlock<String, String>> temperedTransaction = blockchain.getTemperedMessageIfAny();

        assertThat(temperedTransaction.get(0)).isEqualTo(last.getTransaction().get(last.getLastSubBlockHash()));
    }

    @Test
    @DisplayName("getTemperedMessageFromABlockIfAny() passing a tempered block in a tempered multi-file blockchain should return tempered SubBlock ")
    public void getTemperedMessageFromABlockIfAnyPassingATemperedBlockInTemperedMultiFileBlockchainShouldReturnTemperedSubBlock() {

        Blockchain<String, String> blockchain = setupBlockchain();

        Block<String, String> last = blockchain.getBlockFromHash(
                blockchain.getBlockFromHash(blockchain.getLastBlockHash()).getPreviousHashBlock());
        Map<String, SubBlock<String,String>> transactions = last.getTransaction();
        transactions.put(last.getLastSubBlockHash(), new SubBlock<>("dasdasd", "6", "f"));

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new String[]{"10", "11", "12"},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( Long.MAX_VALUE);


        List<SubBlock<String, String>> temperedTransaction = blockchain.
                getTemperedMessageFromABlockIfAny(new String[]{"4", "5", "6"},new String[]{"d", "e", "f"});

        SubBlock<String, String> supposedResult = new SubBlock<>("dasdasd", "6", "f");

        assertAll (
                () -> assertThat(temperedTransaction.get(0)).isEqualTo(supposedResult),
                () -> assertThat(temperedTransaction.size()).isEqualTo(1)
        );

    }

}
