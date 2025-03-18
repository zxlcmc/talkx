package org.bigmouth.gpt.xiaozhi.memory.none;

import org.bigmouth.gpt.xiaozhi.memory.MemoryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Configuration
@ConditionalOnProperty(name = "xiaozhi.memory.type", havingValue = "none")
public class NoneMemoryServiceAutoConfigurer {

    @Bean
    public MemoryService noneMemoryService() {
        return new NoneMemoryService();
    }
}
