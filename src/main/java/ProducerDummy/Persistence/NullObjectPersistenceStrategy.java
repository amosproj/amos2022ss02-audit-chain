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

    int size = 0;
    int current_size = 0;
    public NullObjectPersistenceStrategy(String filepath,String fileName){
        return;
    }
    public NullObjectPersistenceStrategy(String filepath,String fileName,int size){
        this.size = size;
        return;
    }
    @Override
    public void StoreMessage(Message message) {
        // only length of String is interesting
        this.current_size += message.getMessage().getBytes().length;
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
