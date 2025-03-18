package org.bigmouth.gpt.xiaozhi.event.listener;

import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.ai.entity.GptRole;
import org.bigmouth.gpt.entity.UserFriend;
import org.bigmouth.gpt.utils.Constants;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiConfig;
import org.bigmouth.gpt.xiaozhi.event.ChatCompleteEntity;
import org.bigmouth.gpt.xiaozhi.event.ChatCompleteEvent;
import org.bigmouth.gpt.xiaozhi.memory.MemoryServiceFactory;
import org.bigmouth.gpt.xiaozhi.entity.memory.MemoryCreate;
import org.bigmouth.gpt.xiaozhi.entity.memory.MemoryCreateResult;
import org.bigmouth.gpt.xiaozhi.entity.memory.Message;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author Allen Hu
 * @date 2025/3/11
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class MemoryCreateListener implements EventListener<ChatCompleteEvent> {

    private final MemoryServiceFactory memoryServiceFactory;
    private final XiaozhiConfig xiaozhiConfig;

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void consume(ChatCompleteEvent event) {
        UdpClientContext context = event.getContext();
        UserFriend userFriend = context.getUserFriend();
        boolean isSupportMemory = Objects.equals(Constants.YES, userFriend.getIsSupportMemory());
        if (!isSupportMemory) {
            return;
        }
        ChatCompleteEntity completeEntity = event.getChatCompleteEntity();

        // 创建系统消息
//        Message systemMessage = new Message();
//        String systemPrompt = completeEntity.getSystemPrompt();
//        systemMessage.setRole(GptRole.SYSTEM.getName()).setContent(splitPrompt(systemPrompt));

        // 创建用户消息
        Message userMessage = new Message();
        userMessage.setRole(GptRole.USER.getName()).setContent(completeEntity.getUserPrompt());

        // 创建助手消息
//        Message assistantMessage = new Message();
//        assistantMessage.setRole(GptRole.ASSISTANT.getName()).setContent(completeEntity.getCompletion());

        MemoryCreate memoryCreate = new MemoryCreate();
        memoryCreate.setUserId(userFriend.getUserId().toString());
        memoryCreate.setAgentId(userFriend.getId().toString());
        memoryCreate.setMessages(Lists.newArrayList(userMessage));
//        memoryCreate.setMessages(Lists.newArrayList(systemMessage, userMessage, assistantMessage));

        MemoryCreateResult result = memoryServiceFactory.getDefault().addMemory(memoryCreate);

        if (log.isDebugEnabled()) {
            log.debug("Create memory: {}", memoryCreate);
        }
        log.info("[{}] - Create memory: {}", context.getSessionId(), result);
    }

    private String splitPrompt(String prompt) {
        String promptSplitStart = ChatCompletionEventListener.PROMPT_SPLIT_START;
        String promptSplitEnd = ChatCompletionEventListener.PROMPT_SPLIT_END;
        // 删掉指定 split start 和 split end 之间的字符串
        int start = prompt.indexOf(promptSplitStart);
        int end = prompt.indexOf(promptSplitEnd);
        prompt = prompt.substring(0, start) + prompt.substring(end + promptSplitEnd.length());
        return prompt;
    }
}
