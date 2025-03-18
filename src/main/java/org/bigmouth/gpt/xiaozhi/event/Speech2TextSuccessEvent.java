package org.bigmouth.gpt.xiaozhi.event;

import lombok.Getter;
import org.bigmouth.gpt.xiaozhi.entity.UdpHello;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;

import java.util.EventObject;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Getter
public class Speech2TextSuccessEvent extends EventObject {

    private final UdpClientContext context;
    private final UdpHello udpHello;
    private final String text;

    public Speech2TextSuccessEvent(Object source, UdpClientContext context, UdpHello udpHello, String text) {
        super(source);
        this.context = context;
        this.udpHello = udpHello;
        this.text = text;
    }
}
