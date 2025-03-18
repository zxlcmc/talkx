package org.bigmouth.gpt.xiaozhi.event;

import lombok.Getter;
import org.bigmouth.gpt.xiaozhi.entity.DataPacket;

import java.util.EventObject;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Getter
public class ListenStartEvent extends EventObject {

    private final DataPacket dataPacket;

    public ListenStartEvent(Object source, DataPacket dataPacket) {
        super(source);
        this.dataPacket = dataPacket;
    }
}
