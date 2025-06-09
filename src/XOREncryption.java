public class XOREncryption implements ImageEncryption {

    private byte generateKeyByte(int index, String key) {
        return (byte) (key.hashCode() >> (index % 32));
    }

    @Override
    public byte[] encrypt(byte[] data, String key) {
        byte[] encrypted = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            encrypted[i] = (byte) (data[i] ^ generateKeyByte(i, key));
        }
        return encrypted;
    }

    @Override
    public byte[] decrypt(byte[] data, String key) {
        return encrypt(data, key); // XOR є симетричним
    }
}
