package Persistence;

import Messages.AbstractMessage;
import Messages.AggregateJsonMessage;
import Messages.JsonMessage;
import Messages.Message;
import Persistence.FilePersistenceStrategy;

import java.io.*;
import java.nio.file.Files;
import java.util.Vector;

public class AggregateMessages extends FilePersistenceStrategy {

    private static final String fileName = "messages.txt";
    private static int SIZE = 1024;


    /**
     * Constructor for the FilePersistenceStrategy.
     * Set filepath to the path of the file using path and fileName variables and
     * call {@link #CreatePersistenceMechanism()}.
     *
     * @throws IOException if the filepath is not valid
     */
    public AggregateMessages(String path, String fileName) throws IOException {
        super(path, fileName);
    }


    @Override
    public void StoreMessage(int sequenceNumber, String message) {
        try {
            this.fileWriter = new FileWriter(filepath.toString(), true);
            this.fileWriter.write(sequenceNumber + "\r\n");
            this.fileWriter.write(message + "\r\n");
            this.fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public AbstractMessage ReadLastMessage() {

        Vector<Integer> sequence_numbers = new Vector<>();
        Vector<String> messages = new Vector<>();
        AggregateJsonMessage merged_messages = new AggregateJsonMessage();

        File file = new File(this.filepath.toString());
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                int number = Integer.parseInt(line);
                String message = br.readLine();
                merged_messages.addMessage(number, message);
            }
            br.close();

        } catch (IOException e) {
            // there are no values in the file or the file is missing therefore we never did send a Messages.Message to the Broker
            return null;
        }
        merged_messages.formatMessage();


        return merged_messages;
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
