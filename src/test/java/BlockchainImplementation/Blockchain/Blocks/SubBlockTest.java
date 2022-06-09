package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SubBlockTest {

    @Test
    @DisplayName("Creating a new SubBlock should set its properties properly")
    public void creatingANewSubBlockShouldSetItsPropertiesProperly() {
        SubBlock<String, String> subBlock = new SubBlock<>("0", "d", "s");

        assertAll(
                () -> assertThat(subBlock.getMeta_Data()).isEqualTo("d"),
                () -> assertThat(subBlock.getTransaction()).isEqualTo("s"),
                () -> assertThat(subBlock.getPreviousHashBlock()).isEqualTo("0")
        );

    }

    @Test
    @DisplayName("Hash of a new SubBlock should be calculated as H(PrevHash || Meta_Data || Content)")
    public void hashOfANewSubBlockShouldBeCalculatedAsPrevHashPlusMetaDataPlusContent() {
        SubBlock<String, String> subBlock = new SubBlock<>("0", "1", "a");

        assertAll(
                () -> assertThat(subBlock).isNotNull(),
                () -> assertThat(Hasher.hashSHA256("0","1","a")).isEqualTo(subBlock.hashBlock)
        );

    }

}
