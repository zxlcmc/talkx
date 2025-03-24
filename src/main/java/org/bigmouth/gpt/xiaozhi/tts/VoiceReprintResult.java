package org.bigmouth.gpt.xiaozhi.tts;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Allen Hu
 * @date 2025/3/24
 */
@Data
@Accessors(chain = true)
public class VoiceReprintResult {

    /**
     * 语音模型
     */
    private String audioModel;
    /**
     * 语音角色
     */
    private String audioRole;
}
