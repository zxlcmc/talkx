package org.bigmouth.gpt.interceptor;

import org.bigmouth.gpt.entity.User;

/**
 * @author huxiao
 * @date 2023/9/21
 * @since 1.0.0
 */
public class ContextFactory {

    private static final ThreadLocal<Context> THREAD_LOCAL = ThreadLocal.withInitial(Context::new);

    public static Context get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取当前登录的用户
     * @return 用户，游客时返回<code>null</code>
     */
    public static User getLoginUser() {
        return get().getUser();
    }
}
