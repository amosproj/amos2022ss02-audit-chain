package Blockchain.Persistence;

import Blockchain.Messages.AggregateMessage;
import Blockchain.Messages.JsonMessage;
import Blockchain.Messages.Message;

import java.io.*;
import java.nio.file.Files;


/***
 * This is one Implementation which is supposed to guarantee us that we never lose our current state of the Sequence Number.
 */

public class AggregateMessageFilePersistence extends FilePersistenceStrategy {

    private static final String fileName = "messages.txt";
    private static int SIZE = 1024;



    /**
     * Constructor for the FilePersistenceStrategy.
     * Set filepath to the path of the file using path and fileName variables and
     * call {@link #CreatePersistenceMechanism()}.
     *
     * @param path
     * @param fileName
     * @throws IOException if the filepath is not valid
     */
    public AggregateMessageFilePersistence(String path, String fileName) throws IOException {
        super(path, fileName);
    }





    public void StoreMessage(Message message) {
        try {
            this.fileWriter = new FileWriter(filepath.toString(), true);
            this.fileWriter.write(message.getSequence_number() + "\r\n");
            this.fileWriter.write(message.getMessage() + "\r\n");
            this.fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public Message ReadLastMessage() {
        //TODO right now even if there are no Messages an empty Message will be returned, it should return NULL tho

        AggregateMessage messages = new AggregateMessage();

        File file = new File(this.filepath.toString());
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                int number = Integer.parseInt(line);
                String message = br.readLine();
                messages.addMessage(new JsonMessage(number,message));
            }
            br.close();

        } catch (IOException e) {
            // there are no values in the file or the file is missing therefore we never did send a Messages.Message to the Broker
            return null;
        }

        return messages;
    }





    public void cleanFile(){
        try {
            this.fileWriter.close();
        } catch (IOException e) {
            System.out.println("Already Closed");
        }
        File file = new File(this.filepath.toString());
        file.delete();
        this.CreatePersistenceMechanism();
    }

    public boolean isReadyToSend() throws IOException {
        long bytes = Files.size(this.filepath);
        if(bytes <= this.SIZE){
            return false;
        }else{
            return true;
        }
    }

}
