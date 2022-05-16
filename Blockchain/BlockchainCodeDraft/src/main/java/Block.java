public class Block {
    private final String previousHashBlock;
    private String hashBlock;
    private final long timestamp;
    private final String content;
    private int nonce;

    public Block (String previousHashBlock, String content) {
        this.previousHashBlock = previousHashBlock;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
        this.hashBlock = calcHash();
    }

    private String calcHash() {
        return Hasher.hashSHA256(previousHashBlock, Long.toString(timestamp), content);
    }

    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hashBlock.substring(0, prefix).equals(prefixString)) {
            nonce++;
            hashBlock = calcHash();
        }
        return hashBlock;
    }

    public String getHashBlock () {
        return hashBlock;
    }


}
