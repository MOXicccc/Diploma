import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class BlockchainStorage implements StorageSystem {

    // Клас для зберігання блоку
    private static class Block {
        byte[] data;
        int previousHash;
        int currentHash;
        long timestamp;

        Block(byte[] data, int previousHash) {
            this.data = data;
            this.previousHash = previousHash;
            this.timestamp = System.currentTimeMillis();
            this.currentHash = computeHash();
        }

        // Обчислення хеша блоку (імітація)
        int computeHash() {
            return Arrays.hashCode(data) ^ previousHash ^ Long.hashCode(timestamp);
        }
    }

    // Блокчейн у вигляді map для простоти доступу
    private final Map<String, Block> blockchain = new HashMap<>();
    private int latestHash = 0; // Хеш початкового блоку (Genesis block)

    // Збереження даних у блокчейн
    @Override
    public void store(byte[] data, String fileName) throws IOException {
        Block newBlock = new Block(data, latestHash);
        blockchain.put(fileName, newBlock);
        latestHash = newBlock.currentHash;
        System.out.printf("[Blockchain] Блок '%s' успішно збережено. Хеш блоку: %d%n", fileName, latestHash);
    }

    // Завантаження даних з блокчейну
    @Override
    public byte[] load(String fileName) throws IOException {
        Block block = blockchain.get(fileName);
        if (block == null) {
            throw new IOException("[Blockchain] Блок не знайдено: " + fileName);
        }
        System.out.printf("[Blockchain] Блок '%s' завантажено. Хеш блоку: %d%n", fileName, block.currentHash);
        return block.data;
    }
}
