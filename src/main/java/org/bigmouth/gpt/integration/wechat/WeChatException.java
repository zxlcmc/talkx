package org.bigmouth.gpt.integration.wechat;

/**
 * @author huxiao
 * @date 2023/11/6
 * @since 1.0.0
 */
public class WeChatException extends Exception {
    public WeChatException() {
    }

    public WeChatException(String message) {
        super(message);
    }

    public WeChatException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeChatException(Throwable cause) {
        super(cause);
    }

    public WeChatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
