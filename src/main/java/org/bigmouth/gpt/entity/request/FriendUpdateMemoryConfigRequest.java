package org.bigmouth.gpt.entity.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Allen Hu
 * @date 2025/3/11
 */
@Data
public class FriendUpdateMemoryConfigRequest {
    @NotNull(message = "ID不能为空") private Long id;

    /**
     * 是否支持记忆
     */
    private Integer isSupportMemory = 0;
}
