package org.bigmouth.gpt.ai.service;

import com.google.common.collect.Maps;
import org.bigmouth.gpt.ai.ChatService;
import org.bigmouth.gpt.ai.OpenAiModelAdapter;
import org.bigmouth.gpt.ai.entity.ChatServiceArgument;
import org.bigmouth.gpt.exceptions.AiException;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;

/**
 * @author huxiao
 * @date 2023/11/8
 * @since 1.0.0
 */
@Configuration
public class OpenAiChatServiceModelDispatcherImpl implements ApplicationContextAware, ChatService {

    private final Map<String, ChatService> mapping = Maps.newHashMap();
    private ChatService defaultChatService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Collection<OpenAiModelAdapter> adapters = applicationContext.getBeansOfType(OpenAiModelAdapter.class).values();
        adapters.forEach(adapter -> {
            String s = adapter.getRoleType();
            if (adapter instanceof ChatService) {
                ChatService service = (ChatService) adapter;
                if (null == s) {
                    defaultChatService = service;
                } else {
                    mapping.put(s, service);
                }
            }
        });
    }

    @Override
    public Integer platformType() {
        return Constants.AiPlatform.PLATFORM_TYPE_OPENAI;
    }

    @Override
    public void chat(ChatServiceArgument argument) {
        String roleType = argument.getChatRequest().getRoleType();
        ChatService service = mapping.get(roleType);
        if (null == service) {
            service = defaultChatService;
        }
        if (null == service) {
            throw new AiException("不支持的类型。请联系管理员。");
        }
        service.chat(argument);
    }

    @Override
    public String getDefaultRequestUri() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
