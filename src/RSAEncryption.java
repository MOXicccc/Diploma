import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSAEncryption implements ImageEncryption {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    // Генерація ключів (можеш зберігати ці ключі у файлах)
    public RSAEncryption() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGen.initialize(KEY_SIZE);
        KeyPair pair = keyPairGen.generateKeyPair();
        this.publicKey = pair.getPublic();
        this.privateKey = pair.getPrivate();
    }

    @Override
    public byte[] encrypt(byte[] data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    // Методи для отримання публічного та приватного ключів у форматі Base64 (для зручності зберігання)
    public String getPublicKeyBase64() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getPrivateKeyBase64() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
}
