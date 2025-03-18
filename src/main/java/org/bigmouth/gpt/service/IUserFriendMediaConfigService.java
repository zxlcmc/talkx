package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.UserFriendMediaConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bigmouth.gpt.entity.response.AudioConfigVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author allen
 * @since 2025-02-27
 */
public interface IUserFriendMediaConfigService extends IService<UserFriendMediaConfig> {

    UserFriendMediaConfig getByUserFriendId(Long userFriendId);

    AudioConfigVo getDefConfig(String roleType);
}
