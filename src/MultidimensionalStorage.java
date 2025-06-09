import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultidimensionalStorage implements StorageSystem {

    private final int dimensionsCount;

    private final List<Map<String, byte[]>> storageDimensions;


    public MultidimensionalStorage(int dimensionsCount) {
        this.dimensionsCount = dimensionsCount;
        storageDimensions = new ArrayList<>();
        for (int i = 0; i < dimensionsCount; i++) {
            storageDimensions.add(new HashMap<>());
        }
    }

    private int getDimension(String fileName) {
        return Math.abs(fileName.hashCode()) % dimensionsCount;
    }


    @Override
    public void store(byte[] data, String fileName) throws IOException {
        int dimension = getDimension(fileName);
        storageDimensions.get(dimension).put(fileName, data);
        System.out.printf("[Multidimensional] Файл '%s' збережено у вимірі #%d%n", fileName, dimension);
    }

    @Override
    public byte[] load(String fileName) throws IOException {
        int dimension = getDimension(fileName);
        byte[] data = storageDimensions.get(dimension).get(fileName);
        if (data == null) {
            throw new IOException("[Multidimensional] Файл не знайдено: " + fileName);
        }
        System.out.printf("[Multidimensional] Файл '%s' завантажено з виміру #%d%n", fileName, dimension);
        return data;
    }
}
