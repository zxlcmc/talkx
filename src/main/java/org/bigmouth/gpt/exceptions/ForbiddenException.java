package org.bigmouth.gpt.exceptions;

/**
 * 无权访问，需要登录
 * @author allen
 * @date 2023/6/12
 * @since 1.0.0
 */
public class ForbiddenException extends TalkxException {
    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
    }

    public ForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
