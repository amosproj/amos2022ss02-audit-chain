package ConsumerDummy.Client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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


}