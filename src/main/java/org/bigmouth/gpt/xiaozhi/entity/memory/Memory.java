package org.bigmouth.gpt.xiaozhi.entity.memory;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Memory {

    private String id;
    private String memory;
    private String hash;
    private Map<String, Object> metadata;
    private Double score;
    @JsonProperty("created_at")
    @JSONField(name = "created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    @JSONField(name = "updated_at")
    private String updatedAt;
    @JsonProperty("user_id")
    @JSONField(name = "user_id")
    private String userId;
}