package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BlockchainIntSequenceTest {

    private static BlockchainIntSequence<String> setupBlockchain() {

        BlockchainIntSequence<String> blockchain = new BlockchainIntSequence<>("src/test/resources/testOutput/", Long.MAX_VALUE);
        blockchain.addABlock(new Integer[]{1,2,3},
                new String[]{"a", "b", "c"});

        blockchain.addABlock(new Integer[]{4,5,6},
                new String[]{"d", "e", "f"});

        blockchain.addABlock(new Integer[]{7,8,9},
                new String[]{"g", "h", "i"});

        return blockchain;

    }

    @AfterEach
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/blockchain1.json"));
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/blockchain2.json"));
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/blockchain3.json"));
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/lastBlockchain.txt"));
        Files.deleteIfExists(Paths.get("src/test/resources/testOutput/sequenceRecords.csv"));
    }

    @Test
    @DisplayName("getTemperedMessageIfAny() using a valid interval on a tempered blockchain should return the tempered subblock")
    public void getTemperedMessageIfAnyUsingAValidIntervalOnATemperedBlockchainShouldReturnTheTemperedSubBlock() {
        BlockchainIntSequence<String> blockchain = setupBlockchain();

        Block<Integer, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<Integer,String>> transactions = last.getTransaction();

        SubBlock<Integer, String> temperedSubBlock = new SubBlock<>(
                transactions.get(last.getLastSubBlockHash()).getPreviousHashBlock(),
                9,
                "tempered"
        );
        last.getTransaction().put(last.getLastSubBlockHash(), temperedSubBlock);

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{10,11,12},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{13,14,15},
                new String[]{"o", "p", "q"});

        List<SubBlock<Integer, String>> temperedTransaction = blockchain.getTemperedMessageIfAny(13,8);

        assertThat(temperedTransaction.get(0)).isEqualTo(temperedSubBlock);
    }

    @Test
    @DisplayName("getTemperedMessageIfAny() using a valid interval on a tempered blockchain should return the tempered subblocks even if from different parts")
    public void getTemperedMessageIfAnyUsingAValidIntervalOnATemperedBlockchainShouldReturnTheTemperedSubBlocksEvenFromDifferentParts() {
        BlockchainIntSequence<String> blockchain = setupBlockchain();

        Block<Integer, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<Integer,String>> transactions = last.getTransaction();

        SubBlock<Integer, String> temperedSubBlock = new SubBlock<>(
                transactions.get(last.getLastSubBlockHash()).getPreviousHashBlock(),
                9,
                "tempered"
        );
        last.getTransaction().put(last.getLastSubBlockHash(), temperedSubBlock);

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{10,11,12},
                new String[]{"l", "m", "n"});

        last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        transactions = last.getTransaction();
        SubBlock<Integer, String> lastSub = transactions.get(last.getLastSubBlockHash());
        SubBlock<Integer, String> preLastSub = transactions.get(lastSub.getPreviousHashBlock());

        SubBlock<Integer, String> temperedSubBlock2 = new SubBlock<>(
                preLastSub.getPreviousHashBlock(),
                11,
                "tempered"
        );
        last.getTransaction().put(preLastSub.getHashBlock(), temperedSubBlock2);

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{13,14,15},
                new String[]{"o", "p", "q"});

        List<SubBlock<Integer, String>> temperedTransaction = blockchain.getTemperedMessageIfAny(13,8);

        List<SubBlock<Integer, String>> expectedResult = new ArrayList<>(List.of(temperedSubBlock2, temperedSubBlock));

        assertThat(temperedTransaction).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("getTemperedMessageIfAny() using a single interval on a tempered blockchain should return the tempered subblock")
    public void getTemperedMessageIfAnyUsingASingleIntervalOnATemperedBlockchainShouldReturnTheTemperedSubBlock() {
        BlockchainIntSequence<String> blockchain = setupBlockchain();

        Block<Integer, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<Integer,String>> transactions = last.getTransaction();

        SubBlock<Integer, String> temperedSubBlock = new SubBlock<>(
                transactions.get(last.getLastSubBlockHash()).getPreviousHashBlock(),
                9,
                "tempered"
        );
        last.getTransaction().put(last.getLastSubBlockHash(), temperedSubBlock);

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{10,11,12},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{13,14,15},
                new String[]{"o", "p", "q"});

        List<SubBlock<Integer, String>> temperedTransaction = blockchain.getTemperedMessageIfAny(9,9);

        assertThat(temperedTransaction.get(0)).isEqualTo(temperedSubBlock);
    }

    @Test
    @DisplayName("getTemperedMessageIfAny() using a valid interval on a untempered blockchain should return an empty list")
    public void getTemperedMessageIfAnyUsingAValidIntervalOnAUntemperedBlockchainShouldReturnAnEmptyList() {
        BlockchainIntSequence<String> blockchain = setupBlockchain();

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{10,11,12},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{13,14,15},
                new String[]{"o", "p", "q"});

        List<SubBlock<Integer, String>> temperedTransaction = blockchain.getTemperedMessageIfAny(15,1);

        assertThat(temperedTransaction).isEqualTo(new ArrayList<>());
    }

    @Test
    @DisplayName("getTemperedMessageIfAny() using an invalid interval on a tempered blockchain should throw IllegalArgumentException")
    public void getTemperedMessageIfAnyUsingAnInvalidIntervalOnAUntemperedBlockchainShouldReturnAnEmptyList() {
        BlockchainIntSequence<String> blockchain = setupBlockchain();

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{10,11,12},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{13,14,15},
                new String[]{"o", "p", "q"});

        assertThrows(IllegalArgumentException.class, () -> blockchain.getTemperedMessageIfAny(12,13));
    }


    @Test
    @DisplayName("getTemperedMessageIfAny() using a valid interval on a tempered blockchain should return the tempered subblock even if in next parts")
    public void getTemperedMessageIfAnyUsingAValidIntervalOnATemperedBlockchainShouldReturnTheTemperedSubBlockEvenIfNextParts() {
        BlockchainIntSequence<String> blockchain = setupBlockchain();

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{10,11,12},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{13,14,15},
                new String[]{"o", "p", "q"});

        Block<Integer, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<Integer,String>> transactions = last.getTransaction();

        SubBlock<Integer, String> temperedSubBlock = new SubBlock<>(
                transactions.get(last.getLastSubBlockHash()).getPreviousHashBlock(),
                15,
                "tempered"
        );
        last.getTransaction().put(last.getLastSubBlockHash(), temperedSubBlock);

        blockchain.blockchainToJson( Long.MAX_VALUE);

        blockchain.loadPreviousPartBlockchain();
        blockchain.loadPreviousPartBlockchain();

        List<SubBlock<Integer, String>> temperedTransaction = blockchain.getTemperedMessageIfAny(15,14);

        assertThat(temperedTransaction.get(0)).isEqualTo(temperedSubBlock);
    }

    @Test
    @DisplayName("getTemperedMessageIfAny() using a valid interval on a tempered blockchain should return the tempered subblock even if in next parts but not last")
    public void getTemperedMessageIfAnyUsingAValidIntervalOnATemperedBlockchainShouldReturnTheTemperedSubBlockEvenIfNextPartsButNotLast() {
        BlockchainIntSequence<String> blockchain = setupBlockchain();

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{10,11,12},
                new String[]{"l", "m", "n"});

        Block<Integer, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<Integer,String>> transactions = last.getTransaction();

        SubBlock<Integer, String> temperedSubBlock = new SubBlock<>(
                transactions.get(last.getLastSubBlockHash()).getPreviousHashBlock(),
                12,
                "tempered"
        );
        last.getTransaction().put(last.getLastSubBlockHash(), temperedSubBlock);

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{13,14,15},
                new String[]{"o", "p", "q"});

        blockchain.loadPreviousPartBlockchain();
        blockchain.loadPreviousPartBlockchain();

        List<SubBlock<Integer, String>> temperedTransaction = blockchain.getTemperedMessageIfAny(12,11);

        assertThat(temperedTransaction.get(0)).isEqualTo(temperedSubBlock);
    }

}
