package ProducerDummy.Persistence;

import ProducerDummy.Messages.*;

import java.io.*;
import java.nio.file.Files;

public class AggregateHmacMessageFilePersistence extends FilePersistenceStrategy{

    private static final String fileName = "messages.txt";
    private static int SIZE = 1024;
    private static String NEWLINE = "\r\n";

    public AggregateHmacMessageFilePersistence(String path, String fileName) throws IOException {
        super(path, fileName);
    }




    public void StoreMessage(Hmac_Message message) {
        try {
            this.fileWriter = new FileWriter(filepath.toString(), true);
            this.fileWriter.write(message.getSequence_number() + NEWLINE);
            this.fileWriter.write(message.getMessage() + NEWLINE);
            this.fileWriter.write(message.getHmac() + NEWLINE);
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
                String hmac = br.readLine();
                messages.addMessage(new Hmac_Message_JsonMessage(number,message,hmac));
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
