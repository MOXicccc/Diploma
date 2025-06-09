import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class AESEncryption implements ImageEncryption {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private SecretKeySpec getKey(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(password.getBytes("UTF-8"));
        return new SecretKeySpec(key, "AES");
    }

    private IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    @Override
    public byte[] encrypt(byte[] data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = getKey(key);
        IvParameterSpec iv = generateIV();

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedData = cipher.doFinal(data);

        byte[] result = new byte[iv.getIV().length + encryptedData.length];
        System.arraycopy(iv.getIV(), 0, result, 0, iv.getIV().length);
        System.arraycopy(encryptedData, 0, result, iv.getIV().length, encryptedData.length);
        return result;
    }

    @Override
    public byte[] decrypt(byte[] data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = getKey(key);

        byte[] iv = Arrays.copyOfRange(data, 0, 16);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] encryptedData = Arrays.copyOfRange(data, 16, data.length);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        return cipher.doFinal(encryptedData);
    }
}
