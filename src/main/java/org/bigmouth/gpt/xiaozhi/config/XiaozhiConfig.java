package org.bigmouth.gpt.xiaozhi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Allen Hu
 * @date 2025/2/20
 */
@Data
@ConfigurationProperties(prefix = "xiaozhi")
public class XiaozhiConfig {

    /**
     * 默认模型
     */
    private String defaultLlmModel = "qwen-turbo";
}
