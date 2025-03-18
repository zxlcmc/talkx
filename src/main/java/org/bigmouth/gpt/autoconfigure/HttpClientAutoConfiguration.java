package org.bigmouth.gpt.autoconfigure;

import org.apache.http.client.HttpClient;
import org.bigmouth.gpt.utils.HttpClientHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huxiao
 * @date 2023/11/27
 * @since 1.0.0
 */
@Configuration
public class HttpClientAutoConfiguration {

    @Bean
    public HttpClient newHttpClient() {
        return HttpClientHelper.createHttpClient(50, 5, 2000, 5000, 30000);
    }
}
