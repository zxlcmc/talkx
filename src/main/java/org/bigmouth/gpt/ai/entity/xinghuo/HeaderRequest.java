package org.bigmouth.gpt.ai.entity.xinghuo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huxiao
 * @date 2023/10/13
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class HeaderRequest {
    @JSONField(name = "app_id")
    private String appId;
    private String uid;
}
