package org.bigmouth.gpt.xiaozhi.tts;

import com.bxm.warcar.utils.AbstractBeanBus;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/2
 */
@Configuration
public class VoiceReprintServiceFactory extends AbstractBeanBus<TtsPlatformType, VoiceReprintService> {
    @Override
    protected Class<VoiceReprintService> getInstanceClazz() {
        return VoiceReprintService.class;
    }

    @Override
    protected TtsPlatformType getKey(String beanName, VoiceReprintService bean) {
        return bean.of();
    }
}
