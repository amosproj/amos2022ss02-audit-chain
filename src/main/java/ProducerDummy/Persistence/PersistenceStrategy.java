package ProducerDummy.Persistence;

import java.nio.file.Path;
import java.util.ArrayList;

import ProducerDummy.Messages.Message;

/**
 * Interface for the Persistence strategy Mechanism
 * */
public interface PersistenceStrategy {

    /***
     * Store the Message into your Persistence Mechanism e.g. File or Database.
     *
     * @param message message to be stored as a string
     */
    public void StoreMessage(Message message) throws NullPointerException;

    /**
     * Create a persistence mechanism, e.g. create a File or a Database.
     */
    public void CreatePersistenceMechanism();

    /**
     * @return ArrayList with all Messages.
     */
    public ArrayList<Message> ReadLastMessage();

    public Path getFilePath();

    public void cleanFile();

}