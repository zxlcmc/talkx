package org.bigmouth.gpt.xiaozhi.tts;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Allen Hu
 * @date 2025/3/24
 */
@Data
@Accessors(chain = true)
public class VoiceReprintRequest {
    /**
     * 原始音频地址
     */
    private String voiceSrcUrl;
    /**
     * 自定义模型名称前缀
     */
    private String modelNamePrefix;
}
