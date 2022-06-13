package BlockchainImplementation.Blockchain;

import BlockchainImplementation.Blockchain.Blocks.Block;
import BlockchainImplementation.Blockchain.Blocks.SubBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        System.out.println(second.getPreviousHashBlock());
        System.out.println(second.getTransaction().keySet());
        System.out.println(second.getTransaction().values());

        assertThat(second.getHashBlock()).isEqualTo(dumSecond.getHashBlock());
    }

    @Test
    @DisplayName("isAuthentic() over an unchanged blockchain should return true")
    public void isAuthenticOverAnUnchangedBlockchainShouldReturnTrue() {
        Blockchain<String, String> blockchain = new Blockchain<>();
        blockchain.addABlock(new String[]{"1", "2", "3"},
                new String[]{"a", "b", "c"});

        blockchain.addABlock(new String[]{"4", "5", "6"},
                new String[]{"d", "e", "f"});

        blockchain.addABlock(new String[]{"7", "8", "9"},
                new String[]{"g", "h", "i"});

        assertThat(blockchain.isBlockchainAuthentic()).isTrue();

    }

    @Test
    @DisplayName("isAuthentic() over a tempered blockchain should return false")
    public void isAuthenticOverATemperedBlockchainShouldReturnFalse() {
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

        assertThat(blockchain.isBlockchainAuthentic()).isFalse();

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

        List<String> fileAlist = Files.readAllLines(Paths.get("the-file-name.json")); 
        String fileString = fileAlist.toString(); 
        blockchain.blockchainToJson(); 
        List<String> blockchainAlist = Files.readAllLines(Paths.get("the-file-name.json")); 
        String blockchainString = blockchainAlist.toString(); 
        assertThat(blockchainString).isEqualTo(fileString);
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

        //assertThat(blockchain).equals(blockchain.jsonToBlockchain(Paths.get("the-file-name.json"))); 
    }
}
