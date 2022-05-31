package ConsumerDummyBlockchain;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConsumerClientBlockchain client = new ConsumerClientBlockchain();
        client.start();
        return;
    }


}
