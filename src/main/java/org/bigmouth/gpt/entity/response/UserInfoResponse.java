package org.bigmouth.gpt.entity.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author tangxiao
 * @date 2023/7/24
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class UserInfoResponse {

    private Long id;
    private String email;

    private String phoneNum;

    private String name;

    /**
     * 选择的模型
     */
    private String model;

    /**
     * 模型展示图标
     */
    private String modelIcon;

    private String proxyBaseUrl;
    /**
     * 用户的api key
     */
    private String apiKey;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请链接
     */
    private String inviteUrl;

    /**
     * 蒜粒数量
     */
    private BigDecimal coins;

    private String websocketApiKey;

    private boolean allowUpload;
}
