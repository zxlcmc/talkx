package org.bigmouth.gpt.xiaozhi.tts;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Allen Hu
 * @date 2025/2/27
 */
@Data
@Builder
public class TtsData {

    /**
     * 文本
     */
    private String text;
    /**
     * PCM
     */
    private byte[] pcm;
    /**
     * OPUS
     */
    private List<byte[]> opus;

    /**
     * 这个包是否需要发送TTS段落消息
     */
    private boolean sendTtsSentenceStart;
}
