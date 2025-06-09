import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageConverter {

    // збереження будь-яких байтів у файл
    public static void saveBytes(byte[] data, String filePath) throws IOException {
        Files.write(Paths.get(filePath), data);
    }

    // читання будь-яких байтів із файлу
    public static byte[] readBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    // Метод для конвертації зображення в масив байтів
    public static byte[] imageToByteArray(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String format = getFileExtension(imagePath);
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    // Метод для збереження масиву байтів назад у зображення
    public static void byteArrayToImage(byte[] imageData, String outputPath) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        BufferedImage image = ImageIO.read(bais);
        String format = getFileExtension(outputPath);
        ImageIO.write(image, format, new File(outputPath));
    }

    // Допоміжний метод для отримання розширення файлу
    private static String getFileExtension(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }
}