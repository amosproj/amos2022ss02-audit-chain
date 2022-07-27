package ConsumerDummy.Client;

import java.io.IOException;



/**
 * Implementation of a Consumer. It is not really needed, but for uniformity with the Producer the Consumer also has a Client Class which should be used/extended instead of the Consumer
 */
public class  Client extends Consumer {

    public Client(String host, int port, String username, String password,int gui_port) throws IOException {
        super(host, port, username, password,gui_port);
    }

    public Client(String host, int port, String username, String password, int gui_port, String key, String algorithm) {
        super(host, port, username, password,gui_port,key,algorithm);
    }



}