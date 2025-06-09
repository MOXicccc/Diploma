import java.io.IOException;

public interface StorageSystem {
    void store(byte[] data, String fileName) throws IOException;
    byte[] load(String fileName) throws IOException;
}