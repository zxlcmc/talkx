package org.bigmouth.gpt.xiaozhi.mqtt.unsupported;

import org.bigmouth.gpt.xiaozhi.autoconfigure.XiaozhiAutoConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/21
 */
@Configuration
@ConditionalOnMissingBean(XiaozhiAutoConfigurer.class)
public class UnsupportedMqttServiceAutoConfigurer {

    @Bean
    public UnsupportedMqttService unsupportedMqttService() {
        return new UnsupportedMqttService();
    }
}
