package org.bigmouth.gpt.xiaozhi.audio;

import lombok.extern.slf4j.Slf4j;
import org.concentus.OpusDecoder;
import org.concentus.OpusException;

/**
 * @author Allen Hu
 * @date 2025/2/23
 */
@Slf4j
public class OpusDecoderUtils {

    private final OpusDecoder decoder;
    private final int sampleRate;
    private final int channels;
    private final int frameSize;

    public OpusDecoderUtils(int sampleRate, int channels, int frameSizeMs) {
        try {
            this.sampleRate = sampleRate;
            this.channels = channels;
            this.frameSize = (sampleRate * frameSizeMs) / 1000;
            decoder = new OpusDecoder(sampleRate, channels);
        } catch (OpusException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decodeOpusPacket(byte[] opusPacket) {
        try {
            short[] pcmBuffer = new short[frameSize * channels]; // 16-bit PCM
            int decodedSamples = decoder.decode(opusPacket, 0, opusPacket.length, pcmBuffer, 0, frameSize, false);

            if (decodedSamples <= 0) {
                throw new RuntimeException("Opus 解码失败！");
            }

            return shortsToBytes(pcmBuffer);
        } catch (OpusException e) {
            log.error("Opus 解码失败！", e);
            return null;
        }
    }

    public void close() {
    }

    public static byte[] shortsToBytes(short[] input) {
        return shortsToBytes(input, 0, input.length);
    }

    public static byte[] shortsToBytes(short[] input, int offset, int length) {
        byte[] processedValues = new byte[length * 2];
        for (int c = 0; c < length; c++) {
            processedValues[c * 2] = (byte) (input[c + offset] & 0xFF);
            processedValues[c * 2 + 1] = (byte) ((input[c + offset] >> 8) & 0xFF);
        }

        return processedValues;
    }
}
