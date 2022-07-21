package ProducerDummy.Persistence;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import ProducerDummy.Messages.Message;

/***
 * Just like the name suggest this Implementation does not really do anything.
 * It´s best use if you don´t want to store anything Messages.
 */

public class NullObjectPersistenceStrategy implements PersistenceStrategy {
    public NullObjectPersistenceStrategy(String filepath, String fileName) {
        return;
    }

    @Override
    public void StoreMessage(Message message) {
        return;
    }

    @Override
    public void CreatePersistenceMechanism() {
        return;
    }

    @Override
    public ArrayList<Message> ReadLastMessage() {
        return new ArrayList<Message>(0);
    }

    @Override
    public Path getFilePath() {
        return Paths.get(System.getProperty("user.dir"));
    }

    @Override
    public void cleanFile() {
        return;
    }


}
