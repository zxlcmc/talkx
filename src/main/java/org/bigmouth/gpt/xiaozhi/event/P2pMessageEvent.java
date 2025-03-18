package org.bigmouth.gpt.xiaozhi.event;

import com.bxm.warcar.utils.JsonHelper;
import lombok.Getter;
import org.bigmouth.gpt.xiaozhi.entity.DataPacket;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;

import java.util.EventObject;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Getter
public class P2pMessageEvent extends EventObject {

    private final UdpClientContext context;
    private final String messageText;
    private DataPacket message;

    public P2pMessageEvent(Object source, UdpClientContext context, DataPacket message) {
        this(source, context, JsonHelper.convert(message));
        this.message = message;
    }

    public P2pMessageEvent(Object source, UdpClientContext context, String message) {
        super(source);
        this.context = context;
        this.messageText = message;
    }
}
