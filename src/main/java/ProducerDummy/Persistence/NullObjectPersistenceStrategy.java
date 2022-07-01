package ProducerDummy.Persistence;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Vector;

import ProducerDummy.Messages.Message;
import ProducerDummy.Messages.SimpleMessage;

/***
 * This Implementation should be used for Debug purpose because you don´t want to write every message into your Hdd.
 */

public class NullObjectPersistenceStrategy implements PersistenceStrategy {


    public NullObjectPersistenceStrategy(String filepath,String fileName){
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
        return new ArrayList<>(0);
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