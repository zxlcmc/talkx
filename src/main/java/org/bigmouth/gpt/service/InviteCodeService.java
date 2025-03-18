package org.bigmouth.gpt.service;

/**
 * @author huxiao
 * @date 2023/9/20
 * @since 1.0.0
 */
public interface InviteCodeService {

    /**
     * 获取一个邀请码
     * @return 在所有用户的唯一邀请码
     */
    String getInviteCode();
}
