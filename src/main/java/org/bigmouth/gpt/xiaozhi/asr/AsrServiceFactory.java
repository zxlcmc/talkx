package org.bigmouth.gpt.xiaozhi.asr;

import com.bxm.warcar.utils.AbstractBeanBus;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiAsrConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Configuration
public class AsrServiceFactory extends AbstractBeanBus<AsrType, AsrService> {

    private final XiaozhiAsrConfig xiaozhiAsrConfig;

    public AsrServiceFactory(XiaozhiAsrConfig xiaozhiAsrConfig) {
        this.xiaozhiAsrConfig = xiaozhiAsrConfig;
    }

    @Override
    protected Class<AsrService> getInstanceClazz() {
        return AsrService.class;
    }

    @Override
    protected AsrType getKey(String beanName, AsrService bean) {
        return bean.of();
    }

    public AsrService get() {
        return get(xiaozhiAsrConfig.getType());
    }
}
