import java.util.*;

public class DNAEncryption implements ImageEncryption {

    private final Map<String, Character> binaryToDNAMap = Map.of(
            "00", 'A', "01", 'C', "10", 'G', "11", 'T'
    );

    private final Map<Character, String> DNAToBinaryMap = Map.of(
            'A', "00", 'C', "01", 'G', "10", 'T', "11"
    );

    // Генерація хаотичної послідовності на основі логістичного відображення
    private int[] generateChaoticSequence(int length, double key) {
        double r = 3.99;
        double x = key;
        int[] sequence = new int[length];

        for (int i = 0; i < length; i++) {
            x = r * x * (1 - x);
            sequence[i] = (int)(Math.floor(x * length)) % length;
        }
        return sequence;
    }

    // Конвертація байтів у ДНК-послідовність
    private char[] byteArrayToDNA(byte[] data) {
        StringBuilder dnaBuilder = new StringBuilder();
        for (byte b : data) {
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            for (int i = 0; i < 8; i += 2) {
                String twoBits = binaryString.substring(i, i + 2);
                dnaBuilder.append(binaryToDNAMap.get(twoBits));
            }
        }
        return dnaBuilder.toString().toCharArray();
    }

    // Конвертація ДНК-послідовності назад у байти
    private byte[] DNAToByteArray(char[] dnaData) {
        List<Byte> byteList = new ArrayList<>();
        for (int i = 0; i < dnaData.length; i += 4) {
            StringBuilder binaryString = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                binaryString.append(DNAToBinaryMap.get(dnaData[i + j]));
            }
            byte b = (byte) Integer.parseInt(binaryString.toString(), 2);
            byteList.add(b);
        }
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

    // Перемішування на основі хаотичної послідовності
    private void shuffleDNA(char[] dna, int[] chaoticSequence) {
        for (int i = dna.length - 1; i >= 0; i--) {
            int swapIndex = chaoticSequence[i];
            char temp = dna[i];
            dna[i] = dna[swapIndex];
            dna[swapIndex] = temp;
        }
    }

    // Зворотнє перемішування для дешифрування
    private void unshuffleDNA(char[] dna, int[] chaoticSequence) {
        for (int i = 0; i < dna.length; i++) {
            int swapIndex = chaoticSequence[i];
            char temp = dna[i];
            dna[i] = dna[swapIndex];
            dna[swapIndex] = temp;
        }
    }

    @Override
    public byte[] encrypt(byte[] data, String key) {
        double numericKey = Math.abs(key.hashCode() % 1000) / 1000.0; // перетворення ключа в число від 0 до 1
        char[] dna = byteArrayToDNA(data);
        int[] chaoticSequence = generateChaoticSequence(dna.length, numericKey);
        shuffleDNA(dna, chaoticSequence);
        return DNAToByteArray(dna);
    }

    @Override
    public byte[] decrypt(byte[] data, String key) {
        double numericKey = Math.abs(key.hashCode() % 1000) / 1000.0;
        char[] dna = byteArrayToDNA(data);
        int[] chaoticSequence = generateChaoticSequence(dna.length, numericKey);
        unshuffleDNA(dna, chaoticSequence);
        return DNAToByteArray(dna);
    }
}