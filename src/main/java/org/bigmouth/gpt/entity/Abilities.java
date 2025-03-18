package org.bigmouth.gpt.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2023-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Abilities implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("`group`")
    private String group;

    private String model;

    private Long channelId;

    private Boolean enabled;

    private Long priority;


    public static final String GROUP = "group";

    public static final String MODEL = "model";

    public static final String CHANNEL_ID = "channel_id";

    public static final String ENABLED = "enabled";

    public static final String PRIORITY = "priority";

}
