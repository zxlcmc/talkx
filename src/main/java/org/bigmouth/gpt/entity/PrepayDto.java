package org.bigmouth.gpt.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huxiao
 * @date 2023/11/3
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class PrepayDto {

    private int payType;
    private int clientType;
    private String orderId;
    private String openId;
    private String clientIp;
}
