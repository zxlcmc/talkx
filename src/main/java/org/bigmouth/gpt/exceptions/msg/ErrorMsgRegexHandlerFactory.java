package org.bigmouth.gpt.exceptions.msg;

import com.bxm.warcar.utils.AbstractBeanBus;
import org.springframework.context.annotation.Configuration;

/**
 * @author allen
 * @date 2023/7/13
 * @since 1.0.0
 */
@Configuration
public class ErrorMsgRegexHandlerFactory extends AbstractBeanBus<String, ErrorMsgRegexHandler> {

    @Override
    protected Class<ErrorMsgRegexHandler> getInstanceClazz() {
        return ErrorMsgRegexHandler.class;
    }

    @Override
    protected String getKey(String beanName, ErrorMsgRegexHandler bean) {
        return beanName;
    }
}
