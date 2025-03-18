package org.bigmouth.gpt.xiaozhi.audio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * pcm转为wav
 */
public class PcmToWavUtil {

    /**
     * 采样率
     */
    private static final Integer RATE = 16000; // 常用8000或16000，根据讯飞语音合成的实际参数进行调整

    /**
     * 声道
     */
    private static final Integer CHANNELS = 1; // 单声道，根据讯飞语音合成的实际参数进行调整

    /**
     * pcm转wav
     */
    public static byte[] pcmToWav(byte[] pcmBytes) {
        return addHeader(pcmBytes, buildHeader(pcmBytes.length));
    }

    /**
     * 添加头部
     */
    private static byte[] addHeader(byte[] pcmBytes, byte[] headerBytes) {
        byte[] result = new byte[44 + pcmBytes.length];
        System.arraycopy(headerBytes, 0, result, 0, 44);
        System.arraycopy(pcmBytes, 0, result, 44, pcmBytes.length);
        return result;
    }

    /**
     * 删除头部
     * @param wavBytes
     * @return
     */
    public static byte[] removeHeader(byte[] wavBytes) {
        byte[] result = new byte[wavBytes.length - 44];
        System.arraycopy(wavBytes, 44, result, 0, wavBytes.length - 44);
        return result;
    }

    /**
     * 构建头部（共44字节）
     */
    private static byte[] buildHeader(Integer dataLength) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            writeChar(bos, new char[]{'R', 'I', 'F', 'F'});
            writeInt(bos, dataLength + (44 - 8));
            writeChar(bos, new char[]{'W', 'A', 'V', 'E'});
            writeChar(bos, new char[]{'f', 'm', 't', ' '});
            writeInt(bos, 16);
            writeShort(bos, 0x0001);
            writeShort(bos, CHANNELS);
            writeInt(bos, RATE);
            writeInt(bos, (short) (CHANNELS * 2) * RATE);
            writeShort(bos, (short) (CHANNELS * 2));
            writeShort(bos, 16);
            writeChar(bos, new char[]{'d', 'a', 't', 'a'});
            writeInt(bos, dataLength);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入2字节short数据
     */
    private static void writeShort(ByteArrayOutputStream bos, int s) throws IOException {
        byte[] arr = new byte[2];
        arr[1] = (byte) ((s << 16) >> 24);
        arr[0] = (byte) ((s << 24) >> 24);
        bos.write(arr);
    }

    /**
     * 写入4字节int数据
     */
    private static void writeInt(ByteArrayOutputStream bos, int n) throws IOException {
        byte[] buf = new byte[4];
        buf[3] = (byte) (n >> 24);
        buf[2] = (byte) ((n << 8) >> 24);
        buf[1] = (byte) ((n << 16) >> 24);
        buf[0] = (byte) ((n << 24) >> 24);
        bos.write(buf);
    }

    /**
     * 写入1字节char数据
     */
    private static void writeChar(ByteArrayOutputStream bos, char[] id) {
        for (char c : id) {
            bos.write(c);
        }
    }

}