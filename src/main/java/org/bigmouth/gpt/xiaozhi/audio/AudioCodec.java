package org.bigmouth.gpt.xiaozhi.audio;

import cn.hutool.core.util.HexUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * @author Allen Hu
 * @date 2025/2/21
 */
public class AudioCodec {

    private static final int NONCE_SIZE = 16;

    public static AudioPacket decrypt(byte[] encryptedData, String key) throws Exception {
        if (encryptedData.length < NONCE_SIZE) {
            throw new IllegalArgumentException("Invalid encrypted data size");
        }

        // 提取 nonce（前16字节）
        byte[] nonce = new byte[NONCE_SIZE];
        System.arraycopy(encryptedData, 0, nonce, 0, NONCE_SIZE);

        // 提取加密数据
        byte[] encrypted = new byte[encryptedData.length - NONCE_SIZE];
        System.arraycopy(encryptedData, NONCE_SIZE, encrypted, 0, encrypted.length);

        // 解析数据大小和序列号（与C++代码对应）
        int dataSize = ((nonce[2] & 0xFF) << 8) | (nonce[3] & 0xFF);
        long sequence = ((long) (nonce[12] & 0xFF) << 24) |
                ((long) (nonce[13] & 0xFF) << 16) |
                ((long) (nonce[14] & 0xFF) << 8) |
                (nonce[15] & 0xFF);

        // 创建解密器
        SecretKeySpec secretKey = new SecretKeySpec(HexUtil.decodeHex(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        IvParameterSpec ivSpec = new IvParameterSpec(nonce);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // 解密
        byte[] decrypted = cipher.doFinal(encrypted);
        return AudioPacket.builder()
                .sequence(sequence)
                .size(dataSize)
                .nonce(nonce)
                .data(decrypted)
                .build();
    }

    public static byte[] encrypt(byte[] opusData, String key, String baseNonceHex, int sequence) throws Exception {
        if (baseNonceHex.length() != NONCE_SIZE * 2) {
            throw new IllegalArgumentException("Invalid nonce length");
        }

        // 解析 Hex 格式的 baseNonce
        byte[] nonce = HexUtil.decodeHex(baseNonceHex);

        // **填充 dataSize 到 nonce[2] ~ nonce[3]**
        int dataSize = opusData.length;
        nonce[2] = (byte) ((dataSize >> 8) & 0xFF);
        nonce[3] = (byte) (dataSize & 0xFF);

        // **填充 sequence 到 nonce[12] ~ nonce[15]**
        ByteBuffer.wrap(nonce, 12, 4).order(ByteOrder.BIG_ENDIAN).putInt(sequence);

        // **AES 加密**
        SecretKeySpec secretKey = new SecretKeySpec(HexUtil.decodeHex(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        IvParameterSpec ivSpec = new IvParameterSpec(nonce);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedData = cipher.doFinal(opusData);

        // **拼接 nonce + 加密后的 Opus**
        byte[] finalPacket = new byte[NONCE_SIZE + encryptedData.length];
        System.arraycopy(nonce, 0, finalPacket, 0, NONCE_SIZE);
        System.arraycopy(encryptedData, 0, finalPacket, NONCE_SIZE, encryptedData.length);

        return finalPacket;
    }

    public static void main(String[] args) {
        try {
            // 示例：测试加密
            String key = "0123456789abcdef0123456789abcdef"; // 32字节Hex格式AES密钥
            String nonce = "01000000a085e3e1553434eb00000000"; // 16字节Hex格式nonce
            byte[] opusData = "TestOpusData".getBytes(); // 示例Opus数据

            byte[] encryptedPacket = encrypt(opusData, key, nonce, 1);
            System.out.println("加密结果: " + HexUtil.encodeHexStr(encryptedPacket));

            AudioPacket packet = decrypt(encryptedPacket, key);
            System.out.println(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
