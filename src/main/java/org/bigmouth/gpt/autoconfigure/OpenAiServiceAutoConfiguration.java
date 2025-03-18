package org.bigmouth.gpt.autoconfigure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.theokanning.openai.function.FunctionDefinition;
import com.theokanning.openai.function.FunctionExecutorManager;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author huxiao
 * @since 1.0.0
 */
@Configuration
public class OpenAiServiceAutoConfiguration {

    public static ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
    ;

    public static FunctionExecutorManager createFunctionExecutorManager(List<FunctionDefinition> functionDefinitionList) {
        return new FunctionExecutorManager(MAPPER, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()), functionDefinitionList);
    }
}
