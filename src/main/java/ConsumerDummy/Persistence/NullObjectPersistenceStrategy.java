package ConsumerDummy.Persistence;


import ConsumerDummy.Messages.Message;
import ConsumerDummy.Messages.SimpleMessage;

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
    public Message ReadLastMessage() {
        return new SimpleMessage(0, "DEBUG");
    }
}
