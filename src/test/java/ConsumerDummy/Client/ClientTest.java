package ConsumerDummy.Client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Test
    @DisplayName("Testing constructor")
    void constructor() {
        try {
            Client c = new Client("localhost", 5672, "shouldn't be", "set in factory", 0);
            assertNotNull(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Testing constructor")
    void constructorLong() {
        try {
            Client c = new Client("localhost", 5672, "shouldn't be", "set in factory", 0, "key", "algorithm");
            assertNotNull(c);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}