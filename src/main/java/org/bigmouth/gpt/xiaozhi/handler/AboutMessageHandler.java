package org.bigmouth.gpt.xiaozhi.handler;

import org.bigmouth.gpt.xiaozhi.entity.DataPacket;
import org.bigmouth.gpt.xiaozhi.entity.MessageType;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Configuration
public class AboutMessageHandler implements MessageHandler {
    @Override
    public MessageType onType() {
        return MessageType.ABORT;
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return DataPacket.builder()
                .type(MessageType.GOODBYE.getValue())
                .sessionId(dataPacket.getSessionId())
                .build();
    }
}
