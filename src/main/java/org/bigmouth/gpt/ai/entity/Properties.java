package org.bigmouth.gpt.ai.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huxiao
 * @date 2023/9/27
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class Properties {
    private String type;
    private String description;
}
