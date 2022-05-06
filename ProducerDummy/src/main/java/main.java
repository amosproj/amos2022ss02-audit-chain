import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class main {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Client client = new Client();
        client.start();
        return;
    }

}
