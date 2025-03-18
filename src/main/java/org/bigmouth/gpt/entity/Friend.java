package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2024-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer ordered;

    private String avatar;

    private String name;

    @TableField("roleType")
    private String roleType;

    /**
     * AI 类型。1 普通、2 GPTs
     */
    private Integer friendType;

    /**
     * 固定模型
     */
    private String fixedModel;

    /**
     * 提示标签
     */
    private String commentTags;

    /**
     * 快速开始
     */
    private String conversactionStart;

    /**
     * 是否默认启用语音聊天。1 是，其他情况不是
     */
    private Integer voiceChat;

    private String welcome;

    private String intro;

    /**
     * 是否默认添加到用户的好友列表
     */
    private Integer isDefaultFriend;

    /**
     * 是否公开。1 公开，0 不公开
     */
    private Integer isPublic;

    /**
     * 是否需要授权才能添加和使用，0 不需要、1 需要
     */
    private Integer isPermission;

    /**
     * 自定义格式的头像，前端自己定义格式
     */
    private String cssAvatar;

    /**
     * 标签
     */
    private String tag;

    /**
     * 独立设置的请求链接
     */
    private String requestUrl;

    /**
     * 独立请求链接的密钥
     */
    private String apiKey;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

    private Integer deleted;


    public static final String ID = "id";

    public static final String ORDERED = "ordered";

    public static final String AVATAR = "avatar";

    public static final String NAME = "name";

    public static final String ROLETYPE = "roleType";

    public static final String FRIEND_TYPE = "friend_type";

    public static final String FIXED_MODEL = "fixed_model";

    public static final String COMMENT_TAGS = "comment_tags";

    public static final String CONVERSACTION_START = "conversaction_start";

    public static final String VOICE_CHAT = "voice_chat";

    public static final String WELCOME = "welcome";

    public static final String INTRO = "intro";

    public static final String IS_DEFAULT_FRIEND = "is_default_friend";

    public static final String IS_PUBLIC = "is_public";

    public static final String CSS_AVATAR = "css_avatar";

    public static final String TAG = "tag";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

    public static final String DELETED = "deleted";

    public List<String> getConversactionStartSet() {
        return Optional.ofNullable(getConversactionStart())
                .map((Function<String, List<String>>) s -> Lists.newArrayList(StringUtils.split(s, ",")))
                .orElse(null);
    }
}
