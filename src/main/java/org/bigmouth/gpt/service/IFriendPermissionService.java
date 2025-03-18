package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.FriendPermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 好友权限表，私有AI需要该权限才能添加和访问 服务类
 * </p>
 *
 * @author allen
 * @since 2024-09-12
 */
public interface IFriendPermissionService extends IService<FriendPermission> {

    boolean isAuthorized(long friendId, long userId);
}
