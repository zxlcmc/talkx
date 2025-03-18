package org.bigmouth.gpt.ai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bigmouth.gpt.entity.AttachVo;

import java.util.List;

/**
 * @author allen
 * @date 2023-04-20
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class Message {

    private String role;
    private String content;
    private String name;

    /**
     * 文件列表，TalkX消息接口的。跟AI平台无关。
     *
     * 支持反序列化，是接收客户端请求。
     * 不支持序列化，是发送给AI平台时不带这个参数，否则会400。
     */
    @JSONField(serialize = false)
    private List<AttachVo> attachments;
}
