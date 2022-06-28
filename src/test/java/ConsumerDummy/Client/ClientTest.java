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
            Client c = new Client("localhost", 5672, "shouldn't be", "set in factory", "Fake");
            assertNotNull(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test functionallity")
    void start() throws IOException {
        Client c = new Client("localhost", 5672, "shouldn't be", "set in factory", "Fake");
        assertDoesNotThrow(() -> c.start());
    }

}