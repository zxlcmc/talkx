package org.bigmouth.gpt.event;

import org.bigmouth.gpt.exceptions.AiAccountException;

import java.util.EventObject;

/**
 * @author huxiao
 * @date 2023/12/15
 * @since 1.0.0
 */
public class ChatRetryEvent extends EventObject {

    private final AiAccountException exception;

    /**
     * @param source ChatServiceArgument
     * @param exception AiAccountException
     */
    public ChatRetryEvent(Object source, AiAccountException exception) {
        super(source);
        this.exception = exception;
    }

    public AiAccountException getException() {
        return exception;
    }
}
