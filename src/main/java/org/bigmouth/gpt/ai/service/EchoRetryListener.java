package org.bigmouth.gpt.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

/**
 * @author huxiao
 * @date 2023/11/16
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class EchoRetryListener implements RetryListener {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        Throwable lastThrowable = context.getLastThrowable();
        log.warn("Retry-open: {}, {}", lastThrowable.getClass(), lastThrowable.getMessage());
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {

    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {

    }
}
