package org.bigmouth.gpt.autoconfigure.event;

import com.bxm.warcar.integration.eventbus.AsyncEventPark;
import com.bxm.warcar.integration.eventbus.SyncEventPark;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author allen
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(EventParkProperties.class)
public class EventParkAutoConfiguration {

    private final EventParkProperties properties;

    public EventParkAutoConfiguration(EventParkProperties properties) {
        this.properties = properties;
    }

    @Bean(name = { "eventPark", "asyncEventPark" })
    @Primary
    public AsyncEventPark eventPark() {
        return new AsyncEventPark(properties.getAsyncEventBusPoolSize());
    }

    @Bean
    public SyncEventPark syncEventPark() {
        return new SyncEventPark();
    }
}
