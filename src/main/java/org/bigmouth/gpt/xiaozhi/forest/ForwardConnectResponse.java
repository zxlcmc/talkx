package org.bigmouth.gpt.xiaozhi.forest;

import lombok.Data;

/**
 * @author Allen Hu
 * @date 2025/3/18
 */
@Data
public class ForwardConnectResponse {
    private String endpoint;
    private String serverUrl;
    private String clientId;
    private String username;
    private String password;
}
