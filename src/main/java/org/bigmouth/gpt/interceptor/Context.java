package org.bigmouth.gpt.interceptor;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bigmouth.gpt.entity.User;

/**
 * 上下文。应用内在同一个请求线程里是共享的。
 *
 * @author huxiao
 * @date 2023/9/21
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class Context {

    /**
     * 用户
     */
    private User user;

    private String ip;

    private String sessionId;
}
