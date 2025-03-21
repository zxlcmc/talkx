package org.bigmouth.gpt.xiaozhi.config;

import lombok.Data;
import org.bigmouth.gpt.xiaozhi.tts.TtsPlatformType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Allen Hu
 * @date 2025/3/21
 */
@Data
@ConfigurationProperties(prefix = "xiaozhi.talkx")
public class XiaozhiTalkXConfig {

    /**
     * 默认语音合成平台
     */
    private TtsPlatformType defaultTtsPlatformType = TtsPlatformType.ByteDance;
    /**
     * 默认语音模型
     */
    private String defaultVoiceModel;
    /**
     * 默认语音角色
     */
    private String defaultVoiceRole = "zh_female_wanwanxiaohe_moon_bigtts";

    /**
     * 语音合成流式缓冲区大小，单位：字节
     */
    private int ttsStreamBufferSize = 8000;
}
