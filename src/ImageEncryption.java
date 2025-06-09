public interface ImageEncryption {
    byte[] encrypt(byte[] data, String key) throws Exception;
    byte[] decrypt(byte[] data, String key) throws Exception;
}