package org.bigmouth.gpt.xiaozhi.udp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Data
@Accessors(chain = true)
public class ClientAddress {
    private String ip;
    private int port;
}
