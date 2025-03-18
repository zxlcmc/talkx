package org.bigmouth.gpt.scheduler.gpts;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */
@Data
public class Info {
    private String id;
    private String title;
    private String description;

    @JsonAlias("display_type")
    @JSONField(name = "display_type")
    private String displayType;

    @JsonAlias("display_group")
    @JSONField(name = "display_group")
    private String displayGroup;

    private String locale;
}

