package ProducerDummy.DataGeneration;

public class NullObjectDataReader implements DataGenerator{
    @Override
    public String getData() {
        return "Please Change DataGenerator";
    }

    @Override
    public String getData(int sequence_number) {
        return "Please Change DataGenerator";
    }
}
