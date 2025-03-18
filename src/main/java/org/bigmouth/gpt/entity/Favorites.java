package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2024-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Favorites implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String sessionId;

    private Long sessionMessageId;

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

    /**
     * 原始消息的时间
     */
    private LocalDateTime createTime;

    /**
     * 用户收藏的时间
     */
    private LocalDateTime favoriteTime;

    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String SESSION_ID = "session_id";

    public static final String SESSION_MESSAGE_ID = "session_message_id";

    public static final String ROLE = "role";

    public static final String CONTENT = "content";

    public static final String STATUS = "status";

    public static final String IS_INCLUDE_ATTACHS = "is_include_attachs";

    public static final String CREATE_TIME = "create_time";

    public static final String FAVORITE_TIME = "favorite_time";

    public static final String MODIFY_TIME = "modify_time";

}
