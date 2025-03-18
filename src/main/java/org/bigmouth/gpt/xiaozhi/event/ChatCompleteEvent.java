package org.bigmouth.gpt.xiaozhi.event;

import lombok.Getter;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;

import java.util.EventObject;

/**
 * @author Allen Hu
 * @date 2025/3/11
 */
@Getter
public class ChatCompleteEvent extends EventObject {

    private final UdpClientContext context;
    private final ChatCompleteEntity chatCompleteEntity;

    public ChatCompleteEvent(Object source, UdpClientContext context, ChatCompleteEntity chatCompleteEntity) {
        super(source);
        this.context = context;
        this.chatCompleteEntity = chatCompleteEntity;
    }
}
