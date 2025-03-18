package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息附件关联表
 * </p>
 *
 * @author allen
 * @since 2024-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("session_message_attach_ref")
public class SessionMessageAttachRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long msgId;

    /**
     * 附件ID
     */
    private Long attachId;


    public static final String MSG_ID = "msg_id";

    public static final String ATTACH_ID = "attach_id";

}
