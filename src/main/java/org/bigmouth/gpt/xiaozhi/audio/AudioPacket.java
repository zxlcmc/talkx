package org.bigmouth.gpt.xiaozhi.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Allen Hu
 * @date 2025/2/21
 */
@Data
@AllArgsConstructor
@Builder
public class AudioPacket {

    /**
     * 客户端的音频数据包序号
     */
    private long sequence;

    /**
     * 客户端的原始音频数据大小
     */
    private int size;

    /**
     * 数据包的前16个字节，包含随机数和序号及数据包大小。
     * 就像这样：01000000a085e3e1553434eb00000000
     */
    private byte[] nonce;

    /**
     * 客户端的原始音频二进制，一般是：OPUS
     */
    private byte[] data;

    /**
     * 经过转码后的PCM音频二进制
     */
    private byte[] pcm;
}
