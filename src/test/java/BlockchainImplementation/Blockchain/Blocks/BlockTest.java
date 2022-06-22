package BlockchainImplementation.Blockchain.Blocks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BlockTest {

    @Test
    @DisplayName("Creating a new Block should set its previous hash block properly")
    public void creatingANewBlockShouldSetItsPropertiesProperly() {
        Block<String, String> block = new Block<>("0",
                new String[]{"1"},
                new String[]{"a"});

        assertThat(block.getPreviousHashBlock()).isEqualTo("0");

    }

    @Test
    @DisplayName("A new Block should be composed of a proper hashmap of transactions")
    public void aNewBlockShouldBeComposedOfAProperHashMapOfTransactions() {
        Block<String, String> block = new Block<>("0",
                                                      new String[]{"1","2","3"},
                                                      new String[]{"a","b","c"});

        Map<String, SubBlock<String,String>> transactions = new HashMap<>();
        SubBlock<String,String> subBlock1 = new SubBlock<>("0", "1", "a");
        SubBlock<String,String> subBlock2 = new SubBlock<>(subBlock1.hashBlock, "2", "b");
        SubBlock<String,String> subBlock3 = new SubBlock<>(subBlock2.hashBlock, "3", "c");
        transactions.put(subBlock1.hashBlock, subBlock1);
        transactions.put(subBlock2.hashBlock, subBlock2);
        transactions.put(subBlock3.hashBlock, subBlock3);

        assertThat(block.getTransaction().keySet()).isEqualTo(transactions.keySet());

    }

    @Test
    @DisplayName("A new Block should be composed of a proper hashmap of transactions respecting the order in which they are given")
    public void aNewBlockShouldBeComposedOfAProperHashMapOfTransactionsRespectingOrderGiven() {
        Block<String, String> block = new Block<>("0",
                new String[]{"1","2","3"},
                new String[]{"a","b","c"});

        Map<String, SubBlock<String,String>> transactions = new HashMap<>();
        SubBlock<String,String> subBlock1 = new SubBlock<>("0", "1", "a");
        SubBlock<String,String> subBlock2 = new SubBlock<>(subBlock1.hashBlock, "2", "b");
        SubBlock<String,String> subBlock3 = new SubBlock<>(subBlock2.hashBlock, "3", "c");
        transactions.put(subBlock1.hashBlock, subBlock1);
        transactions.put(subBlock2.hashBlock, subBlock2);
        transactions.put(subBlock3.hashBlock, subBlock3);

        SubBlock<String,String> subBlockLast = block.getTransaction().get(block.getLastSubBlockHash());
        SubBlock<String,String> subBlockLastSecond = block.getTransaction().get(subBlockLast.getPreviousHashBlock());
        SubBlock<String,String> subBlockLastThird = block.getTransaction().get(subBlockLastSecond.getPreviousHashBlock());

        assertAll(
                () -> assertThat(subBlockLast.hashBlock).isEqualTo(subBlock3.hashBlock),
                () -> assertThat(subBlockLastSecond.hashBlock).isEqualTo(subBlock2.hashBlock),
                () -> assertThat(subBlockLastThird.hashBlock).isEqualTo(subBlock1.hashBlock)
        );

    }

    @Test
    @DisplayName("getTemperedTransaction() over a unchanged block should return an empty List")
    public void getTemperedTransactionOverAUnchangedBlockShouldReturnAnEmptyList() {
        Block<String, String> block = new Block<>("0",
                new String[]{"1","2","3"},
                new String[]{"a","b","c"});

        assertThat(block.getTemperedMessageIfAny().size()).isEqualTo(0);

    }

    @Test
    @DisplayName("getTemperedTransaction() over a tempered block should return the List with subBlocks tempered")
    public void getTemperedTransactionOverATemperedBlockShouldReturnTheListWithSubBlocksTempered() {
        Block<String, String> block = new Block<>("0",
                new String[]{"1","2","3"},
                new String[]{"a","b","c"});

        Map<String, SubBlock<String,String>> transactions = block.getTransaction();

        transactions.put(block.getLastSubBlockHash(), new SubBlock<>("0", "4", "d"));

        List<SubBlock<String, String>> temperedTransaction = block.getTemperedMessageIfAny();

        assertThat(temperedTransaction.get(0)).isEqualTo(block.getTransaction().get(block.getLastSubBlockHash()));

    }

}
