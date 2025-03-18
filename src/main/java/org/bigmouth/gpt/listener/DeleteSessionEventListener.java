package org.bigmouth.gpt.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.entity.Session;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.event.DeleteUserFriendEvent;
import org.bigmouth.gpt.service.ISessionService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

/**
 * @author huxiao
 * @date 2023/12/13
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class DeleteSessionEventListener implements EventListener<DeleteUserFriendEvent> {

    private final ISessionService sessionService;

    public DeleteSessionEventListener(ISessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void consume(DeleteUserFriendEvent event) {
        User user = event.getUser();
        Long friendId = event.getUserFriend().getFriendId();
        if (Objects.equals(friendId, Constants.IDE_PLUGIN_DEFAULT_FRIEND_ID)) {
            log.warn("不能删除IDE插件的默认好友，否则历史话题将被删除。");
            return;
        }
        List<Session> sessions = sessionService.list(Wrappers.query(new Session().setUserId(user.getId()).setFriendId(friendId)));
        for (Session session : sessions) {
            sessionService.deleteWithMessages(session.getId());
        }
    }
}
