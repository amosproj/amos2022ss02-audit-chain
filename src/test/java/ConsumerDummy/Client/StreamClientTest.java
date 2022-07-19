package ConsumerDummy.Client;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StreamClientTest {

    @Test
    void start() throws IOException {
        StreamClient s = new StreamClient("localhost", 5672, "guest", "guest",9999);
        assertThrows(NullPointerException.class, () -> s.start());
    }

    @Test
    void beforeACK() throws IOException {
        StreamClient s = new StreamClient("localhost", 5672, "guest", "guest",9999);
        assertDoesNotThrow(() -> s.BeforeACK(null));
    }

    @Test
    void recoverOffset() throws IOException {
        StreamClient s = new StreamClient("localhost", 5672, "guest", "guest",9999);
        assertDoesNotThrow(() -> s.RecoverOffset());
    }
}