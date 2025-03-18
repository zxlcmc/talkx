package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Allen Hu
 * @since 2023-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("session_message")
public class SessionMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String sessionId;

    /**
     * user / assistant
     */
    private String role;

    private String content;

    /**
     * 消息状态。200 正常，500 异常消息
     */
    private Integer status;

    /**
     * 是否包含附件，1 包含
     */
    private Integer isIncludeAttachs;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

    // ----- 下面的参数不是该对象本身存在数据库的 ----- //

    @TableField(exist = false)
    private List<AttachVo> attachments;

    public static final String ID = "id";

    public static final String SESSION_ID = "session_id";

    public static final String ROLE = "role";

    public static final String CONTENT = "content";

    public static final String STATUS = "status";

    public static final String IS_INCLUDE_ATTACHS = "is_include_attachs";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
