package org.bigmouth.gpt.xiaozhi.memory;

import com.bxm.warcar.utils.AbstractBeanBus;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/11
 */
@Configuration
public class MemoryServiceFactory extends AbstractBeanBus<MemoryType, MemoryService> {
    @Override
    protected Class<MemoryService> getInstanceClazz() {
        return MemoryService.class;
    }

    @Override
    protected MemoryType getKey(String beanName, MemoryService bean) {
        return bean.of();
    }

    public MemoryService getDefault() {
        return getAll().iterator().next();
    }
}
