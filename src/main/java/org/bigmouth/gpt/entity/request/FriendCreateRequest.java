package org.bigmouth.gpt.entity.request;

import lombok.Data;
import org.bigmouth.gpt.utils.Constants;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author huxiao
 * @date 2023/12/13
 * @since 1.0.0
 */
@Data
public class FriendCreateRequest {
    /**
     * TalkX产品类型
     * 0 Web版
     * 1 移动版
     * 2 IDE插件版
     */
    private int productType = Constants.PRODUCT_TYPE_WEB;

    /**
     * 头像地址
     */
    private String avatar = Constants.DEFAULT_AVATAR_URL;

    /**
     * 自定义格式的头像，前端自己定义格式
     */
    private String cssAvatar;

    /**
     * 好友名称
     */
    @NotBlank(message = "昵称不能为空") private String name;

    /**
     * 简介
     */
    private String intro;

    /**
     * 招呼语
     */
    private String welcome;

    /**
     * 是否支持语音聊天。1 是，其他情况否。
     */
    private Integer voiceChat = 0;

    /**
     * 标签
     */
    private String tag;

    /**
     * 指令
     */
    private String systemPrompt;

    /**
     * 上下文数量
     */
    private int messageContextSize = 32;

    /**
     * 快速开始
     */
    private List<String> conversationStart;

    /**
     * 模型设置
     */
    private FriendCreateModelConfig openaiRequestBody = new FriendCreateModelConfig();
}
