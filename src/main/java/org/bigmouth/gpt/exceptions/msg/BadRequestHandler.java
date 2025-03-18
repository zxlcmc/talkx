package org.bigmouth.gpt.exceptions.msg;

import org.bigmouth.gpt.exceptions.AiAccountException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author huxiao
 * @date 2023/12/7
 * @since 1.0.0
 */
@Configuration
public class BadRequestHandler implements ErrorMsgRegexHandler {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage(AiAccountException exception) {
        return exception.getMessage();
    }
}
