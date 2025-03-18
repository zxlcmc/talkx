package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("aigc_images")
public class AigcImages implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String model;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户提示词
     */
    private String userPrompt;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 系统提示词
     */
    private String revisedPrompt;

    private Integer width;

    private Integer height;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String MODEL = "model";

    public static final String USER_ID = "user_id";

    public static final String USER_PROMPT = "user_prompt";

    public static final String IMAGE_URL = "image_url";

    public static final String REVISED_PROMPT = "revised_prompt";

    public static final String WIDTH = "width";

    public static final String HEIGHT = "height";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
