package org.bigmouth.gpt.xiaozhi.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.ai.agent.Agent;
import org.bigmouth.gpt.ai.agent.AgentRequest;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContextHolder;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author Allen Hu
 * @date 2025/3/4
 */
@Slf4j
@Configuration
public class GoodbyeAgent implements Agent<GoodbyeAgent.GoodbyeRequest> {

    private final UdpClientContextHolder udpClientContextHolder;

    public GoodbyeAgent(UdpClientContextHolder udpClientContextHolder) {
        this.udpClientContextHolder = udpClientContextHolder;
    }

    @Override
    public String getFunctionName() {
        return "execute_goodbye_command";
    }

    @Override
    public String getFunctionDescription() {
        return "如果用户说再见或拜拜之类想结束对话的想法。请执行这个函数，然后跟用户说再见。";
    }

    @Override
    public Object apply(GoodbyeRequest goodbyeRequest) {
        String sessionId = goodbyeRequest.getSessionId();
        UdpClientContext context = udpClientContextHolder.get(sessionId);
        if (Objects.isNull(context)) {
            log.warn("Not found context of sessionId: {}", sessionId);
            return new GoodbyeResponse();
        }
        context.setSayGoodbye(true);
        return new GoodbyeResponse();
    }

    @Data
    public static class GoodbyeRequest implements AgentRequest {
        @JsonProperty(required = true)
        @JsonPropertyDescription("本次对话的会话ID")
        private String sessionId;
    }

    @Data
    public static class GoodbyeResponse {
    }
}
