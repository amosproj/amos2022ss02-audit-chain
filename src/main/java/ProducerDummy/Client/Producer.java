package ProducerDummy.Client;

import ProducerDummy.DataGeneration.DataGenerator;
import ProducerDummy.Persistence.PersistenceStrategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Producer extends AbstractClient {

    /**
     * Every Producer must be able to store their Messages into a File and also must have a Datasource
     * */
    protected DataGenerator dataGenerator;
    protected PersistenceStrategy persistenceStrategy;

    static int START_NUMBER = 0;

    protected int sequence_number;

    /**
     * Constructor for Client.AbstractClient. Initializes the filepath, the file reader and set information for the
     * connection factory. Call {@link #initFactory()} to initialize the connection factory.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param queue_name
     */
    public Producer(String host, int port, String username, String password, String queue_name) {
        super(host, port, username, password, queue_name);
    }

    public void setDataGenerator(DataGenerator dataGenerator){
        this.dataGenerator = dataGenerator;
    };

    public void setPersistenceStrategy(PersistenceStrategy persistenceStrategy){
     this.persistenceStrategy = persistenceStrategy;
     recoverLastState();
    }

    public void recoverLastState() {
        try {
            this.sequence_number = this.persistenceStrategy.ReadLastMessage().getSequence_number();
        } catch (Exception e) {
            this.sequence_number = START_NUMBER;
            return;
        }
        this.dataGenerator.getData(this.sequence_number);
    }

    public static byte[] serialize(Object object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(object);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
