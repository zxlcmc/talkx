package org.bigmouth.gpt.exceptions;

/**
 * 用户蒜粒不足
 *
 * @author huxiao
 * @date 2023/9/21
 * @since 1.0.0
 */
public class CoinNotEnoughException extends TalkxException {

    public CoinNotEnoughException() {
    }

    public CoinNotEnoughException(String message) {
        super(message);
    }

    public CoinNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoinNotEnoughException(Throwable cause) {
        super(cause);
    }

    public CoinNotEnoughException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
