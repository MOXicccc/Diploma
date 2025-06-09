import java.util.Random;

public class FisherYatesEncryption implements ImageEncryption {

    private int[] generatePermutation(int length, String key) {
        int[] perm = new int[length];
        for (int i = 0; i < length; i++) perm[i] = i;
        Random rnd = new Random(key.hashCode());
        for (int i = length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = perm[i];
            perm[i] = perm[j];
            perm[j] = tmp;
        }
        return perm;
    }

    @Override
    public byte[] encrypt(byte[] data, String key) {
        int[] perm = generatePermutation(data.length, key);
        byte[] encrypted = new byte[data.length];
        for (int i = 0; i < data.length; i++) encrypted[i] = data[perm[i]];
        return encrypted;
    }

    @Override
    public byte[] decrypt(byte[] data, String key) {
        int[] perm = generatePermutation(data.length, key);
        byte[] decrypted = new byte[data.length];
        for (int i = 0; i < data.length; i++) decrypted[perm[i]] = data[i];
        return decrypted;
    }
}
