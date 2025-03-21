package org.bigmouth.gpt.xiaozhi.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/21
 */
@Configuration
@ConditionalOnProperty(name = "xiaozhi.enable", havingValue = "true")
public class XiaozhiAutoConfigurer {
}
