package org.bigmouth.gpt.xiaozhi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/2/21
 */
@Configuration
@EnableConfigurationProperties({
        XiaozhiConfig.class,
        XiaozhiMqttConfig.class,
        XiaozhiUdpConfig.class,
        XiaozhiSileroConfig.class,
        XiaozhiAlibabaConfig.class,
        XiaozhiByteDanceConfig.class,
        XiaozhiMemoryConfig.class,
        XiaozhiAsrConfig.class
})
public class XiaozhiAutoConfigurer {
}
