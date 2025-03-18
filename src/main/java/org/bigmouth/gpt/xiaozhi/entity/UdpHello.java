package org.bigmouth.gpt.xiaozhi.entity;

import lombok.Data;

/**
 * @author Allen Hu
 * @date 2025/2/21
 */
@Data
public class UdpHello {

    private DataPacket request;
    private DataPacket response;

    public String getSessionId() {
        return response.getSessionId();
    }
}
