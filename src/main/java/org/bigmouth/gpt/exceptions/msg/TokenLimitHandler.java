package org.bigmouth.gpt.exceptions.msg;

import org.bigmouth.gpt.exceptions.AiAccountException;
import org.springframework.context.annotation.Configuration;

/**
 * @author allen
 * @date 2023/7/13
 * @since 1.0.0
 */
@Configuration
public class TokenLimitHandler implements ErrorMsgRegexHandler {

    @Override
    public String getRegex() {
        return "This model's maximum context length is (\\d+) tokens\\.";
    }

    @Override
    public String getMessage(AiAccountException exception) {
        return "抱歉，当前会话的消息内容长度已经超过了这个模型的限制。";
    }
}
