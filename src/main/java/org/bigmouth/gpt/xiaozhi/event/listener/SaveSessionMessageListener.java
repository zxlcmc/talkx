package org.bigmouth.gpt.xiaozhi.event.listener;

import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import lombok.AllArgsConstructor;
import org.bigmouth.gpt.ai.entity.GptRole;
import org.bigmouth.gpt.entity.Session;
import org.bigmouth.gpt.entity.SessionMessage;
import org.bigmouth.gpt.service.ISessionMessageService;
import org.bigmouth.gpt.service.ISessionService;
import org.bigmouth.gpt.xiaozhi.event.ChatCompleteEntity;
import org.bigmouth.gpt.xiaozhi.event.ChatCompleteEvent;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/11
 */
@Configuration
@AllArgsConstructor
public class SaveSessionMessageListener implements EventListener<ChatCompleteEvent> {

    private final ISessionService sessionService;
    private final ISessionMessageService sessionMessageService;

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void consume(ChatCompleteEvent event) {
        ChatCompleteEntity endEntity = event.getChatCompleteEntity();
        Session session = endEntity.getSession();
        String prompt = endEntity.getUserPrompt();
        String completion = endEntity.getCompletion();
        sessionMessageService.save(new SessionMessage().setSessionId(session.getId()).setRole(GptRole.USER.getName()).setContent(prompt));
        sessionMessageService.save(new SessionMessage().setSessionId(session.getId()).setRole(GptRole.ASSISTANT.getName()).setContent(completion));
        sessionService.saveOrUpdate(session);
    }
}
