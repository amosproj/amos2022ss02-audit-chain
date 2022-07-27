package ProducerDummy.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ProducerDummy.Messages.SimpleMessage;
import com.rabbitmq.client.Channel;

import ProducerDummy.DataGeneration.NullObjectDataReader;
import ProducerDummy.Messages.Message;
import ProducerDummy.Persistence.NullObjectPersistenceStrategy;

/**
 * Client implementation of a Producer which is able to send Messages to the RabbitMQ
 */
public class Client extends Producer {

    private final static int SECOND_DELAY_BETWEEN_MESSAGES = 1;


    public Client(String host, int port, String username, String password) throws IOException {
        super(host, port, username, password);
        this.dataGenerator = new NullObjectDataReader();
        this.persistenceStrategy = new NullObjectPersistenceStrategy("nopath", "nofile");

    }

    public Client(String host, int port, String username, String password, int desired_payload_in_byte) {
        super(host, port, username, password);
        this.DESIRED_PAYLOAD_IN_BYTE = desired_payload_in_byte;
    }


    /***
     * 1. Get Channel, 2. Get stored messages from PersistenceStrategy, 3. send these if ready to send.
     * 4. get new Data, store these in persistence, update Payload and send to server if ready to send
     *
     * @throws IOException if an I/O error occurs
     * @throws TimeoutException if the timeout expires
     * @throws InterruptedException if the thread is interrupted
     */
    public void start() throws IOException, TimeoutException, InterruptedException {
        Channel channel = this.getChannel();
        ArrayList<Message> messageVector = this.persistenceStrategy.ReadLastMessage();
        this.RecoverCurrentPayloadSize(messageVector);
        // check if the Message(s) in the file shall be sent to rabbitmq
        if (messageVector.size() > 0) {
            if (isReadyToSend()) {
                System.out.println("Found Message(s) in the Persistence Storage which are ready to send, they will now be send");
                this.getAcknowledgment(channel, messageVector);
                this.sequence_number = messageVector.get(messageVector.size() - 1).getSequence_number() + 2;
                this.persistenceStrategy.cleanFile();
                messageVector.clear();
            }else{
                // Persistence Storage has Messages (s), we can recover the current event Number
                this.sequence_number = messageVector.get(messageVector.size() - 1).getSequence_number() + 2;
            }
        }
        // Step One (maybe) sending Messages which were stored in the Persistence Mechanism is done.
        // Now go back to the normal behaviour and create/receive events and send these to the queue
        for (String line = this.dataGenerator.getData(); line != null; line = this.dataGenerator.getData()) {
            /***
             * Create the Message, store it into the PersistenceMechanism and add it to the List.
             * Now decide whether to send the List or not.
             * Strategy are either to send every Message as one or collect a certain amount of messages and send them later
             */

            Message message = this.createMessage(sequence_number,line);
            this.persistenceStrategy.StoreMessage(message);
            messageVector.add(message);
            this.updatePayloadSize(message);
            if (isReadyToSend()) {
                System.out.println(String.format("Event Message(s) from %d to %d are ready to send, trying to send them to RabbitMq ...",
                        messageVector.get(0).getSequence_number(),
                        messageVector.get(messageVector.size() - 1).getSequence_number()
                ));
                this.getAcknowledgment(channel, messageVector);
                System.out.println("Message(s) were sent successfully!");
                this.persistenceStrategy.cleanFile();
                messageVector.clear();
                TimeUnit.SECONDS.sleep(SECOND_DELAY_BETWEEN_MESSAGES);

            } else {
                System.out.println(String.format("Not yet Ready to send Message(s). The Message with the Event number %d was appended ",
                        message.getSequence_number()
                ));
            }
            this.sequence_number++;
        }
        channel.close();
    }


    protected Message createMessage(int sequence_number,String message_string) {
        return new SimpleMessage(sequence_number,message_string);
    }




}


