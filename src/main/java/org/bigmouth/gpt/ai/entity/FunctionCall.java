package org.bigmouth.gpt.ai.entity;

import lombok.Data;

/**
 * @author huxiao
 * @date 2023/9/27
 * @since 1.0.0
 */
@Data
public class FunctionCall {
    private String name;
    private String arguments;
}
