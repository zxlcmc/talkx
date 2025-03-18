package org.bigmouth.gpt.exceptions;

/**
 * AI平台状态异常，一般指AI平台关闭、故障、没有可用的密钥、未知的roleType类型。
 *
 * @author huxiao
 * @date 2023/10/25
 * @since 1.0.0
 */
public class AiStatusException extends AiException {
    public AiStatusException() {
    }

    public AiStatusException(String message) {
        super(message);
    }

    public AiStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiStatusException(Throwable cause) {
        super(cause);
    }

    public AiStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
