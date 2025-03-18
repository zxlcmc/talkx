package org.bigmouth.gpt.autoconfigure.cache;

import com.bxm.warcar.cache.Counter;
import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.cache.impls.redis.JedisCounter;
import com.bxm.warcar.cache.impls.redis.JedisFetcher;
import com.bxm.warcar.cache.impls.redis.JedisUpdater;
import org.bigmouth.gpt.autoconfigure.JedisConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import redis.clients.jedis.JedisPool;

@Configuration
@ConditionalOnProperty(name = "config.enable-redis-cache", havingValue = "true")
@EnableConfigurationProperties(JedisConfiguration.class)
public class JedisAutoConfiguration {

    private final JedisConfiguration configuration;

    public JedisAutoConfiguration(JedisConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(configuration, configuration.getHost(), configuration.getPort(), configuration.getTimeout(), configuration.getPassword(), configuration.getDatabase());
    }

    @Bean
    @Primary
    public Fetcher jedisFetcher(JedisPool jedisPool) {
        return new JedisFetcher(jedisPool);
    }

    @Bean
    @Primary
    public Updater jedisUpdater(JedisPool jedisPool) {
        return new JedisUpdater(jedisPool);
    }

    @Bean
    @Primary
    public Counter jedisCounter(JedisPool jedisPool) {
        return new JedisCounter(jedisPool);
    }
}
