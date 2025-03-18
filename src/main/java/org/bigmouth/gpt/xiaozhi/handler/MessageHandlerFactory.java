package org.bigmouth.gpt.xiaozhi.handler;

import com.bxm.warcar.utils.AbstractBeanBus;
import org.bigmouth.gpt.xiaozhi.entity.MessageType;
import org.springframework.context.annotation.Configuration;

/**
 * 消息处理器工厂
 * @author Allen Hu
 * @date 2025/2/20
 */
@Configuration
public class MessageHandlerFactory extends AbstractBeanBus<MessageType, MessageHandler> {

    @Override
    protected Class<MessageHandler> getInstanceClazz() {
        return MessageHandler.class;
    }

    @Override
    protected MessageType getKey(String beanName, MessageHandler bean) {
        return bean.onType();
    }
}