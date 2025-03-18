package org.bigmouth.gpt.autoconfigure;

import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.scheduler.ClearHistorySessionScheduler;
import org.bigmouth.gpt.service.ISessionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2024/10/30
 */
@Configuration
public class ApplicationAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "config.enable-clear-history-session", havingValue = "true")
    public ClearHistorySessionScheduler clearHistorySessionScheduler(ApplicationConfig applicationConfig,
                                                                     ISessionService sessionService) {
        return new ClearHistorySessionScheduler(applicationConfig, sessionService);
    }
}
