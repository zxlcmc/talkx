package org.bigmouth.gpt.exceptions;

/**
 * AI平台网络异常，一般包括连接超时、读取超时、平台网关等情况。
 *
 * @author huxiao
 * @date 2023/10/25
 * @since 1.0.0
 */
public class AiNetworkException extends AiException {
    public AiNetworkException() {
    }

    public AiNetworkException(String message) {
        super(message);
    }

    public AiNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiNetworkException(Throwable cause) {
        super(cause);
    }

    public AiNetworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
