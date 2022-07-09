package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class BlockchainSequenceTest {

    private static BlockchainSequence<String> setupBlockchain() {

        BlockchainSequence<String> blockchain = new BlockchainSequence<>("src/test/resources/testOutput/", Long.MAX_VALUE);
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
        BlockchainSequence<String> blockchain = setupBlockchain();

        System.out.println(blockchain.toString());

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{10,11,12},
                new String[]{"l", "m", "n"});

        blockchain.blockchainToJson( 1);

        blockchain.addABlock(new Integer[]{13,14,15},
                new String[]{"o", "p", "q"});


        blockchain.loadPreviousPartBlockchain();
        blockchain.loadPreviousPartBlockchain();
        System.out.println(blockchain.toString());

        Block<Integer, String> last = blockchain.getBlockFromHash(blockchain.getLastBlockHash());
        Map<String, SubBlock<Integer,String>> transactions = last.getTransaction();
        SubBlock<Integer, String> temperedSubBlock = new SubBlock<>(
                last.getTransaction().get(last.getLastSubBlockHash()).getPreviousHashBlock(),
                9,
                "tempered"
        );
        transactions.put(last.getLastSubBlockHash(), temperedSubBlock);

        blockchain.loadNextPartBlockchain();
        blockchain.loadNextPartBlockchain();

        List<SubBlock<Integer, String>> temperedTransaction = blockchain.getTemperedMessageIfAny(13,8);

        assertThat(temperedTransaction.get(0)).isEqualTo(temperedSubBlock);
    }




}
