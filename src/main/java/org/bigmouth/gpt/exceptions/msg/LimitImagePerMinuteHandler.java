package org.bigmouth.gpt.exceptions.msg;

import org.bigmouth.gpt.exceptions.AiAccountException;
import org.springframework.context.annotation.Configuration;

/**
 * @author huxiao
 * @date 2023/12/26
 * @since 1.0.0
 */
@Configuration
public class LimitImagePerMinuteHandler implements ErrorMsgRegexHandler {
    @Override
    public String getRegex() {
        return "Rate limit exceeded for images per minute";
    }

    @Override
    public String getMessage(AiAccountException exception) {
        return "当前画图请求负载超出了限制，请稍后再试。";
    }
}
