package org.bigmouth.gpt.ai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Allen Hu
 * @date 2024/12/24
 */
@Data
public class StreamOptions {

    @JSONField(name = "include_usage")
    private Boolean includeUsage;
}
