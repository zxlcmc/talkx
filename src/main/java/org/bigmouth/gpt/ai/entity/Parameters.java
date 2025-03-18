package org.bigmouth.gpt.ai.entity;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @author huxiao
 * @date 2023/9/27
 * @since 1.0.0
 */
@Data
public class Parameters {

    private String type = "object";

    private Map<String, Properties> properties = Maps.newHashMap();
}
