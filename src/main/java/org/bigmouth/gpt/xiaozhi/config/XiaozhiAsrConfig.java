package org.bigmouth.gpt.xiaozhi.config;

import lombok.Data;
import org.bigmouth.gpt.xiaozhi.asr.AsrType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Data
@ConfigurationProperties(prefix = "xiaozhi.asr")
public class XiaozhiAsrConfig {

    private AsrType type = AsrType.TALKX;
}
