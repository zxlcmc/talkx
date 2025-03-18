package org.bigmouth.gpt.xiaozhi.event;

import lombok.Getter;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;

import java.util.EventObject;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Getter
public class VadStartEvent extends EventObject {

    private final UdpClientContext context;

    public VadStartEvent(Object source, UdpClientContext context) {
        super(source);
        this.context = context;
    }

    public String getSessionId() {
        return context.getSessionId();
    }
}
