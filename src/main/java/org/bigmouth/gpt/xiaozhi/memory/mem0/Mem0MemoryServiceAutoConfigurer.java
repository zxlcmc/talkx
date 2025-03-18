package org.bigmouth.gpt.xiaozhi.memory.mem0;

import org.bigmouth.gpt.xiaozhi.forest.Mem0Api;
import org.bigmouth.gpt.xiaozhi.memory.MemoryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Configuration
@ConditionalOnProperty(name = "xiaozhi.memory.type", havingValue = "mem0")
public class Mem0MemoryServiceAutoConfigurer {

    @Bean
    public MemoryService noneMemoryService(Mem0Api mem0Api) {
        return new Mem0MemoryServiceImpl(mem0Api);
    }
}
