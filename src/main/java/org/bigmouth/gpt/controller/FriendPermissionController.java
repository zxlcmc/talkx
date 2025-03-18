package org.bigmouth.gpt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.bigmouth.gpt.entity.Friend;
import org.bigmouth.gpt.entity.FriendPermission;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.service.IFriendPermissionService;
import org.bigmouth.gpt.service.IFriendService;
import org.bigmouth.gpt.service.IUserService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author huxiao
 * @date 2024/9/12
 * @since 1.0.0
 */
@RestController
@RequestMapping("/friend_permission")
public class FriendPermissionController {

    private final IUserService userService;
    private final IFriendService friendService;
    private final IFriendPermissionService friendPermissionService;

    public FriendPermissionController(IUserService userService, IFriendService friendService, IFriendPermissionService friendPermissionService) {
        this.userService = userService;
        this.friendService = friendService;
        this.friendPermissionService = friendPermissionService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addPermission(@RequestParam String phoneNum,
                                                @RequestParam Long friendId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(User.PHONE_NUM, phoneNum);
        User user = userService.getOne(wrapper);
        if (user == null) {
            throw new IllegalStateException("用户不存在");
        }
        Friend friend = friendService.getById(friendId);
        if (friend == null) {
            throw new IllegalStateException("好友不存在");
        }
        if (!Objects.equals(Constants.YES, friend.getIsPermission())) {
            throw new IllegalStateException("好友未开启权限");
        }
        friendPermissionService.save(new FriendPermission().setUserId(user.getId()).setFriendId(friendId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deletePermission(@RequestParam String phoneNum,
                                                   @RequestParam Long friendId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(User.PHONE_NUM, phoneNum);
        User user = userService.getOne(wrapper);
        if (user == null) {
            throw new IllegalStateException("用户不存在");
        }
        friendPermissionService.remove(new QueryWrapper<FriendPermission>().eq(FriendPermission.USER_ID, user.getId()).eq(FriendPermission.FRIEND_ID, friendId));
        return ResponseEntity.ok().build();
    }
}
