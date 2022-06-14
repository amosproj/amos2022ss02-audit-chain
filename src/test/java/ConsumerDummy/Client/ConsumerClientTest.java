package ConsumerDummy.Client;

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
                    () -> assertEquals("localhost", c.factory.getHost()),
                    () -> assertEquals(5672, c.factory.getPort()),
                    () -> assertEquals("guest", c.factory.getUsername()),  //should be guest in localhost
                    () -> assertEquals("guest", c.factory.getPassword())); //should be guest in localhost
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}