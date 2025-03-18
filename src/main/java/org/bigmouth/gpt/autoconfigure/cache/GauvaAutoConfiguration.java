package org.bigmouth.gpt.autoconfigure.cache;

import com.bxm.warcar.cache.Counter;
import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.cache.impls.cache.GuavaCounter;
import com.bxm.warcar.cache.impls.cache.GuavaFetcher;
import com.bxm.warcar.cache.impls.cache.GuavaUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Configuration
public class GauvaAutoConfiguration {

    @Bean
    public Fetcher guavaFetcher() {
        return new GuavaFetcher();
    }

    @Bean
    public Updater guavaUpdater() {
        return new GuavaUpdater();
    }

    @Bean
    public Counter guavaCounter() {
        return new GuavaCounter();
    }
}
