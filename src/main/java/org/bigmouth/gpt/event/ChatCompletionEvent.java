package org.bigmouth.gpt.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigmouth.gpt.ai.entity.ApiKey;
import org.bigmouth.gpt.ai.entity.Usage;
import org.bigmouth.gpt.entity.AiModel;
import org.bigmouth.gpt.entity.Prompt;
import org.bigmouth.gpt.entity.User;

import java.util.EventObject;

/**
 * 聊天对话完成事件
 *
 * @author huxiao
 * @date 2023/11/16
 * @since 1.0.0
 */
public class ChatCompletionEvent extends EventObject {

    private final Parameter parameter;
    public ChatCompletionEvent(Object source, Parameter parameter) {
        super(source);
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Parameter {
        private Prompt prompt;
        private User user;
        private ApiKey apiKey;
        private AiModel aiModel;
        private String completion;
        /**
         * 请求消耗的token数
         */
        private Usage usage;
        /**
         * 开始请求的时间
         */
        private Long startNanoTime;
    }
}
