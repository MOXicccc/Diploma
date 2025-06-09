import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HybridStorage implements StorageSystem {

    private static final long CLOUD_THRESHOLD = 100 * 1024; // 100 KB

    @Override
    public void store(byte[] data, String fileName) throws IOException {
        String storagePath;

        Files.createDirectories(Paths.get("storage/local/"));
        Files.createDirectories(Paths.get("storage/cloud/"));

        if (data.length < CLOUD_THRESHOLD) {
            storagePath = "storage/local/" + fileName;
            System.out.println("Файл менше 100KB, збережено ЛОКАЛЬНО.");
        } else {
            storagePath = "storage/cloud/" + fileName;
            System.out.println("Файл більше 100KB, збережено в ХМАРІ.");
        }

        Files.write(Paths.get(storagePath), data);
        System.out.println("Файл збережено: " + storagePath);
    }

    @Override
    public byte[] load(String fileName) throws IOException {
        String localPath = "storage/local/" + fileName;
        String cloudPath = "storage/cloud/" + fileName;

        if (Files.exists(Paths.get(localPath))) {
            System.out.println("Файл знайдено ЛОКАЛЬНО.");
            return Files.readAllBytes(Paths.get(localPath));
        } else if (Files.exists(Paths.get(cloudPath))) {
            System.out.println("Файл знайдено у ХМАРІ.");
            return Files.readAllBytes(Paths.get(cloudPath));
        } else {
            throw new IOException("Файл не знайдено ні локально, ні в хмарі.");
        }
    }
}
