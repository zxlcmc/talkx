package org.bigmouth.gpt.xiaozhi.config;

import lombok.Data;
import org.bigmouth.gpt.xiaozhi.memory.MemoryType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Allen Hu
 * @date 2025/3/11
 */
@Data
@ConfigurationProperties(prefix = "xiaozhi.memory")
public class XiaozhiMemoryConfig {

    private MemoryType type = MemoryType.None;
}
