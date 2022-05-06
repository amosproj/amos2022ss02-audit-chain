package Persistence;

import Messages.AbstractMessage;
import Messages.JsonMeesage;

/***
 * This Implemenation should be used for Debug purpose because you don´t want to write every message into your Hdd
 *
 */

public class NullObjectPersistenceStrategy implements PersistenceStrategy {


    @Override
    public void StoreMessage(int sequenceNumber, String message) {
        return;
    }

    @Override
    public void CreatePersistenceMechanism() {
        return;
    }

    @Override
    public AbstractMessage ReadLastMessage() {
        return new JsonMeesage(0, "DEBUG");
    }
}
