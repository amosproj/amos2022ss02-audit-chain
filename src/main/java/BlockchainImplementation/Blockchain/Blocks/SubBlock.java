package BlockchainImplementation.Blockchain.Blocks;

import BlockchainImplementation.Blockchain.Hashing.Hasher;

import java.io.IOException;

/**
 * A SubBlock is the smallest unit of a Block, it contains the clear information and points to its previous hashed SubBlock.
 *
 * @param <T> The type of the meta_data (it could be everything, e.g. sequence number) of the information contained
 *           in the SubBlock.
 * @param <R> The type of the information contained in the SubBlock.
 */

public class SubBlock<T,R> extends AbstractBlock<R>{

    private final T meta_data; /** contains the meta data of the message */

    public SubBlock(String previousHashBlock, T meta_data, R content) throws IOException {
        super(previousHashBlock, content);
        this.meta_data = meta_data;

        this.hashBlock = calcHash();
    }

    /**
     * Calculates the hash of the current SubBlock just putting all its a properties together
     *
     * @return the hash of the current SubBlock
     */
    @Override
    protected String calcHash() {
        return Hasher.hashSHA256(this.getPreviousHashBlock(), meta_data.toString(), transaction.toString());
    }

    public T getMeta_Data () { return meta_data; }


}
