package ProducerDummy.Persistence;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Vector;

import ProducerDummy.Messages.Message;

/** Interface for the persistence strategy mechanisms */
//TODO generic for readlastmessage
public interface PersistenceStrategy {

    /***
     * Store the Messages.Message into your Persistence Mechanism e.g File or Database.
     *
     * @param message message to be stored as a string
     */
    public void StoreMessage(Message message) throws NullPointerException;

    /**
     * Create a persistence mechanism, e.g create the File or the Database.
     */
    public void CreatePersistenceMechanism();

    /**
     * @return last AbstractMessage written in the persistence mechanism
     */
    public ArrayList<Message> ReadLastMessage();

    public Path getFilePath();

    public void cleanFile();

}