import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class main {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConsumerClient client = new ConsumerClient();
        client.start();
        return;
    }

}
