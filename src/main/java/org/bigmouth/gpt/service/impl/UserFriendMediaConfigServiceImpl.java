package org.bigmouth.gpt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigmouth.gpt.entity.UserFriendMediaConfig;
import org.bigmouth.gpt.entity.response.AudioConfigVo;
import org.bigmouth.gpt.mapper.talkx.UserFriendMediaConfigMapper;
import org.bigmouth.gpt.service.IUserFriendMediaConfigService;
import org.bigmouth.gpt.utils.ResourceFileUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author allen
 * @since 2025-02-27
 */
@Service
public class UserFriendMediaConfigServiceImpl extends ServiceImpl<UserFriendMediaConfigMapper, UserFriendMediaConfig> implements IUserFriendMediaConfigService {

    @Override
    public UserFriendMediaConfig getByUserFriendId(Long userFriendId) {
        QueryWrapper<UserFriendMediaConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserFriendMediaConfig.USER_FRIEND_ID, userFriendId);
        return getOne(queryWrapper);
    }

    @Override
    public AudioConfigVo getDefConfig(String roleType) {
        String json = ResourceFileUtils.fetch("tts-def.json");
        return Optional.ofNullable(json)
                .map(JSONObject::parseObject)
                .map(jsonObject -> jsonObject.getObject(roleType, AudioConfigVo.class))
                .orElse(null);
    }
}
