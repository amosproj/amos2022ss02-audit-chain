import java.util.Date;

public class Block {
    private final String previousHashBlock; /** contains the hash of the previous block */
    private String hashBlock; /** contains the hash of the current block */
    private final long timestamp; /** contains the date and time of when the block was created */
    private final String content; /** content of the current block */
    private int nonce; /** arbitrary number to be used in cryptography */

    public Block (String previousHashBlock, String content) {
        this.previousHashBlock = previousHashBlock;
        this.content = content;
        this.timestamp = new Date().getTime();
        this.hashBlock = calcHash();
    }

    /**
     * Calculates the hash of the current block calling {@link Hasher#hashSHA256} method
     *
     * @return the hash of the current block
     * */
    private String calcHash() {
        return Hasher.hashSHA256(previousHashBlock, Long.toString(timestamp), content);
    }

    /**
     * Mine the current block using a prefix with which the hash has to begin.
     * The nonce is incremented until the hash of the block begins with the prefix of 0's.
     *
     * @param prefix
     * @return the hash of the current block
     */
    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hashBlock.substring(0, prefix).equals(prefixString)) {
            nonce++;
            hashBlock = calcHash();
        }
        return hashBlock;
    }

    /**
     * @return hash of the current block saved in the block
     */
    public String getHashBlock () {
        return hashBlock;
    }


}
