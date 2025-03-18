package org.bigmouth.gpt.event;

import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.entity.UserFriend;

import java.util.EventObject;

/**
 * 删除好友事件
 *
 * @author huxiao
 * @date 2023/12/13
 * @since 1.0.0
 */
public class DeleteUserFriendEvent extends EventObject {

    private final User user;
    private final UserFriend userFriend;

    public User getUser() {
        return user;
    }

    public UserFriend getUserFriend() {
        return userFriend;
    }

    public DeleteUserFriendEvent(Object source, User user, UserFriend userFriend) {
        super(source);
        this.user = user;
        this.userFriend = userFriend;
    }
}
