package org.bigmouth.gpt.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 更新好友多媒体配置请求
 *
 * @author Allen Hu
 * @date 2025/2/28
 */
@Data
public class UpdateMediaConfigRequest {

    @NotNull(message = "用户好友ID不能为空")
    private Long userFriendId;

    @NotBlank(message = "音频平台类型不能为空")
    private String audioPlatformType;

    private String audioModel;

    @NotBlank(message = "音频角色不能为空")
    private String audioRole;

    private String audioDemoUrl;

    /**
     * 是否自定义模型
     */
    private Integer customModel;

    /**
     * 选择的模型
     */
    @NotBlank(message = "模型不能为空")
    private String llmModel;

    private Integer isSupportTool;

    private String proxyBaseUrl;

    private String apiKey;
}