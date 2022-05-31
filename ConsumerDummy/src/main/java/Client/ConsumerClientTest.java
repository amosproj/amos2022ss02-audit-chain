package Client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerClientTest {

    @Test
    @DisplayName("Testing localhost functionality")
    void localhost() {
        try {
            ConsumerClient c = new ConsumerClient("localhost", 5672, " ", " ", "Fake");
            assertAll(
                    () -> assertEquals("localhost",c.HOST),
                    () -> assertEquals(5672,c.PORT),
                    () -> assertEquals(" ", c.USER),
                    () -> assertEquals(" ", c.PASSWORD));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}