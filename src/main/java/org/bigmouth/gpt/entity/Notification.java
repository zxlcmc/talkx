package org.bigmouth.gpt.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息信息
 * </p>
 *
 * @author allen
 * @since 2023-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息类型。1 普通消息
     */
    private Integer type;

    /**
     * 是否置顶。0 不置顶、1 置顶
     */
    private Integer pinned;

    /**
     * 消息文本内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 已删除标志
     */
    private Integer deleted;


    public static final String ID = "id";

    public static final String TITLE = "title";

    public static final String TYPE = "type";

    public static final String PINNED = "pinned";

    public static final String CONTENT = "content";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

    public static final String DELETED = "deleted";

}
