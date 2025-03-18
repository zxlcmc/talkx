package com.bxm.warcar.cache;

/**
 * 值超出限制异常
 *
 * @author allen
 * @date 2017-12-05
 */
public class ValueOutOfLimitException extends RuntimeException {
    public ValueOutOfLimitException() {
        super();
    }

    public ValueOutOfLimitException(String message) {
        super(message);
    }

    public ValueOutOfLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueOutOfLimitException(Throwable cause) {
        super(cause);
    }

    protected ValueOutOfLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
