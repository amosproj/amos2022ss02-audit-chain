package ProducerDummy.Persistence;


import ProducerDummy.Messages.Message;

/** Interface for the persistence strategy mechanisms */
public interface PersistenceStrategy {

    /***
     * Store the Messages.Message into your Persistence Mechanism e.g File or Database.
     *
     * @param sequenceNumber the sequence number of the message
     * @param message message to be stored as a string
     */
    public void StoreMessage(Message message);

    /**
     * Create a persistence mechanism, e.g create the File or the Database.
     */
    public void CreatePersistenceMechanism();

    /**
     * @return last AbstractMessage written in the persistence mechanism
     */

    public Message ReadLastMessage();

}