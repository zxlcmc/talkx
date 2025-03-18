package org.bigmouth.gpt.integration;

import org.bigmouth.gpt.facade.PayOrder;
import org.springframework.context.annotation.Configuration;

/**
 * @author huxiao
 * @date 2023/11/3
 * @since 1.0.0
 */
@Configuration
public class PayIntegration {

    public String pay(PayOrder payOrder) {
        throw new UnsupportedOperationException();
    }

    public void close(PayOrder payOrder) {
        throw new UnsupportedOperationException();
    }
}
