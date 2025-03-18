package org.bigmouth.gpt.exceptions.msg;

import org.bigmouth.gpt.exceptions.AiAccountException;
import org.springframework.context.annotation.Configuration;

/**
 * @author huxiao
 * @date 2024/1/18
 * @since 1.0.0
 */
@Configuration
public class UnsupportImageHandler implements ErrorMsgRegexHandler {
    @Override
    public String getRegex() {
        return "You uploaded an unsupported image";
    }

    @Override
    public String getMessage(AiAccountException exception) {
        return "抱歉，该模型只支持分析图片。或者不支持你上传的图片格式。";
    }

}
