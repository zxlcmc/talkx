package org.bigmouth.gpt.exceptions;

import org.bigmouth.gpt.ai.entity.ApiKey;

/**
 * AI平台限额。
 *
 * @author huxiao
 * @date 2023/10/25
 * @since 1.0.0
 */
public class AiLimitException extends AiAccountException {
    public AiLimitException(String message) {
        super(message);
    }

    public AiLimitException(ApiKey apiKey) {
        super(apiKey);
    }

    public AiLimitException(String message, ApiKey apiKey) {
        super(message, apiKey);
    }

    public AiLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiLimitException(Throwable cause) {
        super(cause);
    }

    public AiLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
