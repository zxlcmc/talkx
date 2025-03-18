package org.bigmouth.gpt.exceptions;

/**
 * @author huxiao
 * @date 2023/10/25
 * @since 1.0.0
 */
public class AiException extends RuntimeException {
    public AiException() {
    }

    public AiException(String message) {
        super(message);
    }

    public AiException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiException(Throwable cause) {
        super(cause);
    }

    public AiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
