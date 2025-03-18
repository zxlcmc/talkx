package org.bigmouth.gpt.autoconfigure.id;

import com.bxm.warcar.id.IdGenerator;
import com.bxm.warcar.id.random.RandomIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author allen
 * @date 2022-03-22
 * @since 1.0
 */
@Configuration
public class RandomIdGeneratorConfiguration {

    @Bean
    public IdGenerator randomIdGenerator() {
        return new RandomIdGenerator();
    }
}
