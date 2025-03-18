package org.bigmouth.gpt.event;

import org.bigmouth.gpt.entity.ChatRequest;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.ai.entity.OpenApiRequest;

import java.util.EventObject;

/**
 * 请求AI平台成功事件
 *
 * @author huxiao
 * @date 2023/8/22
 * @since 1.0.0
 */
public class ChatRequestEvent extends EventObject {
    private final ChatRequest chatRequest;
    private final OpenApiRequest openApiRequest;
    private final User user;

    public ChatRequestEvent(Object source, ChatRequest chatRequest, OpenApiRequest openApiRequest, User user) {
        super(source);
        this.chatRequest = chatRequest;
        this.openApiRequest = openApiRequest;
        this.user = user;
    }

    public OpenApiRequest getOpenApiRequest() {
        return openApiRequest;
    }

    public User getUser() {
        return user;
    }

    public ChatRequest getChatRequest() {
        return chatRequest;
    }
}
