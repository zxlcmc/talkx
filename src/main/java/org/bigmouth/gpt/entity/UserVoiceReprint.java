package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2025-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserVoiceReprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 音频名称
     */
    private String voiceName;

    /**
     * 音频源文件地址
     */
    private String voiceSrcUrl;

    /**
     * 音频平台类型
     */
    private String audioPlatformType;

    /**
     * 音频模型
     */
    private String audioModel;

    /**
     * 音频角色
     */
    private String audioRole;

    /**
     * 音频示例
     */
    private String audioDemoUrl;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String VOICE_NAME = "voice_name";

    public static final String VOICE_SRC_URL = "voice_src_url";

    public static final String AUDIO_PLATFORM_TYPE = "audio_platform_type";

    public static final String AUDIO_MODEL = "audio_model";

    public static final String AUDIO_ROLE = "audio_role";

    public static final String AUDIO_DEMO_URL = "audio_demo_url";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
