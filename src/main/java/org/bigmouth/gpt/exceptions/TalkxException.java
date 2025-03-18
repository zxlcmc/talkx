package org.bigmouth.gpt.exceptions;

/**
 * @author huxiao
 * @date 2023/10/25
 * @since 1.0.0
 */
public class TalkxException extends RuntimeException {

    public TalkxException() {
    }

    public TalkxException(String message) {
        super(message);
    }

    public TalkxException(String message, Throwable cause) {
        super(message, cause);
    }

    public TalkxException(Throwable cause) {
        super(cause);
    }

    public TalkxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
