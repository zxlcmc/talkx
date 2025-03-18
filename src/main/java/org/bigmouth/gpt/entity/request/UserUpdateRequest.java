package org.bigmouth.gpt.entity.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tangxiao
 * @date 2023/7/24
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class UserUpdateRequest {
    /**
     * 更新的字段名
     * 目前支持的字段：name、phone_num、email、api_key、model、avatar、proxyBaseUrl
     */
    @NotBlank
    private String field;
    private String value;
    /**
     * 当更新不同的字段,extra可能表示不一样的含义：
     * 1.phoneNum时 extra为手机验证码
     */
    private String extra;
}
