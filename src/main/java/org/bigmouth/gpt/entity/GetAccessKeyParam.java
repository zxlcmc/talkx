package org.bigmouth.gpt.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huxiao
 * @date 2023/11/8
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class GetAccessKeyParam {

    /**
     * 用户
     */
    private User user;
    /**
     * 优先使用好友配置的地址和密钥
     */
    private Friend friend;
}
