package org.bigmouth.gpt.scheduler.gpts;

import lombok.Data;

/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */
@Data
public class Tool {
    private String id;
    private String type;
    private String settings;
    private String metadata;
}

