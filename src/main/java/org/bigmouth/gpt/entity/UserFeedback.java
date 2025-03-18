package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户反馈
 * </p>
 *
 * @author tangxiao
 * @since 2023-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_feedback")
public class UserFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 图片，多个逗号分隔
     */
    private String image;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String CONTENT = "content";

    public static final String IMAGE = "image";

    public static final String CONTACT_PHONE = "contact_phone";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
