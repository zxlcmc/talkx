package org.bigmouth.gpt.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.entity.Friend;
import org.bigmouth.gpt.entity.PromptConfig;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.entity.UserFriend;
import org.bigmouth.gpt.event.DeleteUserFriendEvent;
import org.bigmouth.gpt.service.IFriendService;
import org.bigmouth.gpt.service.IPromptConfigService;
import org.bigmouth.gpt.service.IUserFriendService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 检查被删除的好友，如果没有任何人关注，则删除掉。
 *
 * @author huxiao
 * @date 2023/12/14
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class DeleteFriendEventListener implements EventListener<DeleteUserFriendEvent> {

    private final IFriendService friendService;
    private final IUserFriendService userFriendService;
    private final IPromptConfigService promptConfigService;

    public DeleteFriendEventListener(IFriendService friendService, IUserFriendService userFriendService, IPromptConfigService promptConfigService) {
        this.friendService = friendService;
        this.userFriendService = userFriendService;
        this.promptConfigService = promptConfigService;
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void consume(DeleteUserFriendEvent event) {
        UserFriend userFriend = event.getUserFriend();
        Long friendId = userFriend.getFriendId();
        User user = event.getUser();
        Friend friend = friendService.getById(friendId);
        if (Objects.equals(Constants.YES, friend.getIsPublic())) {
            // 如果是公开的好友，不能删除。
            return;
        }
        List<UserFriend> friends = userFriendService.list(Wrappers.query(new UserFriend().setFriendId(friendId)));
        Optional<UserFriend> first = friends.stream().filter(new Predicate<UserFriend>() {
            @Override
            public boolean test(UserFriend userFriend) {
                return !Objects.equals(userFriend.getUserId(), user.getId());
            }
        }).findFirst();
        if (first.isPresent()) {
            // 如果存在不是自己的关注，那么不能删除了，等别人删除的时候还会检查。
            return;
        }

        String roleType = userFriend.getRoleType();
        if (!promptConfigService.remove(Wrappers.query(new PromptConfig().setRoleType(roleType)))) {
            log.error("{} Remove fail!", roleType);
        }
        if (!friendService.removeById(friendId)) {
            log.error("{} Remove fail!", friendId);
        }
    }
}
