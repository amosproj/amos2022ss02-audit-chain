package Persistence;

import Messages.AbstractMessage;

public interface PersistenceStrategy {

    /***
     * Store the Messages.Message into your Persistence Mechasnium e.g File or Database
     * @param sequenceNumber
     * @param message
     */
    public void StoreMessage(int sequenceNumber, String message);

    /**
     * e.g Create the File or the Database
     */
    public void CreatePersistenceMechanism();

    public AbstractMessage ReadLastMessage();

}

