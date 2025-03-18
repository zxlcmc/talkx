package org.bigmouth.gpt.xiaozhi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Allen Hu
 * @date 2025/2/26
 */
@Data
@ConfigurationProperties(prefix = "xiaozhi.udp")
public class XiaozhiUdpConfig {

    private String udpServerHost = "172.16.30.29";

    private int udpServerPort = 8884;
}
