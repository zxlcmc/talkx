package org.bigmouth.gpt.autoconfigure.id;

import com.bxm.warcar.id.IdGenerator;
import com.bxm.warcar.id.redis.RedisIdGenerator;
import org.bigmouth.gpt.autoconfigure.JedisConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author allen
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({JedisPool.class, Jedis.class})
@EnableConfigurationProperties({JedisConfiguration.class})
@ConditionalOnProperty(name = "config.enable-redis-cache", havingValue = "true")
public class RedisIdGeneratorConfiguration {

    private final JedisConfiguration configuration;

    public RedisIdGeneratorConfiguration(JedisConfiguration jedisConfiguration) {
        this.configuration = jedisConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean(JedisPool.class)
    public JedisPool jedisPool() {
        return new JedisPool(configuration, configuration.getHost(), configuration.getPort(),
                configuration.getTimeout(), configuration.getPassword(), configuration.getDatabase());
    }

    @Primary
    @Bean
    public IdGenerator redisIdGenerator(JedisPool jedisPool) {
        return new RedisIdGenerator(jedisPool);
    }
}
