package ConsumerDummy.Client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    @DisplayName("Testing localhost functionality")
    void localhost() {
        try {
            AggregateClient c = new AggregateClient("localhost", 5672, "shouldn't be", "set in factory", "Fake");
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