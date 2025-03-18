package org.bigmouth.gpt.xiaozhi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Allen Hu
 * @date 2025/2/26
 */
@Data
@ConfigurationProperties(prefix = "xiaozhi.silero")
public class XiaozhiSileroConfig {

    private String modelPath = "src/main/conf/silero_vad.onnx";
    private float startThreshold = 0.6f;
    private float endThreshold = 0.45f;
    private int minSilenceDurationMs = 2000;
    private int timeoutSec = 10;
}
