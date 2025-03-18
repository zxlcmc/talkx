package org.bigmouth.gpt.xiaozhi.entity.memory;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchRequest {
    /**
     * 搜索查询
     */
    private String query;

    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    @JSONField(name = "user_id")
    private String userId;

    /**
     * 运行ID
     */
    @JsonProperty("run_id")
    @JSONField(name = "run_id")
    private String runId;

    /**
     * 代理ID
     */
    @JsonProperty("agent_id")
    @JSONField(name = "agent_id")
    private String agentId;

    /**
     * 限制返回结果数量
     */
    private Integer limit = 3;
}