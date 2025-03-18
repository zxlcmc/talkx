package org.bigmouth.gpt.exceptions.msg;

import org.bigmouth.gpt.exceptions.AiAccountException;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.context.annotation.Configuration;

/**
 * @author huxiao
 * @date 2023/11/8
 * @since 1.0.0
 */
@Configuration
public class SafetySystemHandler implements ErrorMsgRegexHandler {
    @Override
    public String getRegex() {
        return Constants.Regex.SAFETY_SYSTEM;
    }

    @Override
    public String getMessage(AiAccountException exception) {
        return "由于我们的安全系统，您的请求被拒绝了。您的提示可能包含我们的安全系统不允许的文本。";
    }
}
