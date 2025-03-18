package org.bigmouth.gpt.xiaozhi.entity.memory;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {
    /**
     * 消息角色（用户或助手）
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;
}