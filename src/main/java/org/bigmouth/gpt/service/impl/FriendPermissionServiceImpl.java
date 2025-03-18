package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigmouth.gpt.entity.FriendPermission;
import org.bigmouth.gpt.mapper.talkx.FriendPermissionMapper;
import org.bigmouth.gpt.service.IFriendPermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 好友权限表，私有AI需要该权限才能添加和访问 服务实现类
 * </p>
 *
 * @author allen
 * @since 2024-09-12
 */
@Service
public class FriendPermissionServiceImpl extends ServiceImpl<FriendPermissionMapper, FriendPermission> implements IFriendPermissionService {
    @Override
    public boolean isAuthorized(long friendId, long userId) {
        QueryWrapper<FriendPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FriendPermission.USER_ID, userId);
        queryWrapper.eq(FriendPermission.FRIEND_ID, friendId);
        return count(queryWrapper) > 0;
    }
}
