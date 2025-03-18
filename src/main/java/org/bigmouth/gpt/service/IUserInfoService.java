package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.entity.request.UserUpdateRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tangxiao
 * @since 2023-07-21
 */
public interface IUserInfoService {

    void update(User user, UserUpdateRequest request);
}
