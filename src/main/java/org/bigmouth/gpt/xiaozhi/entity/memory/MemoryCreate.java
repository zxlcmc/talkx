package org.bigmouth.gpt.xiaozhi.entity.memory;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class MemoryCreate {
    /**
     * 消息列表
     */
    private List<Message> messages;

    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    @JSONField(name = "user_id")
    private String userId;

    /**
     * 代理ID
     */
    @JsonProperty("agent_id")
    @JSONField(name = "agent_id")
    private String agentId;

    /**
     * 运行ID
     */
    @JsonProperty("run_id")
    @JSONField(name = "run_id")
    private String runId;

    /**
     * 元数据
     */
    private Map<String, Object> metadata;
}