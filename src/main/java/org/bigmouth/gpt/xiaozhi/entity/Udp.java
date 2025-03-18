package org.bigmouth.gpt.xiaozhi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Allen Hu
 * @date 2025/2/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Udp {

    // {
    //        "server": "120.24.160.13",
    //        "port": 8884,
    //        "encryption": "aes-128-ctr",
    //        "key": "df7bbfcf0927a9aad84d74aa6a5228ed",
    //        "nonce": "01000000a085e3e1553434eb00000000"
    //    }

    private String server;
    private int port;
    private String encryption;
    private String key;
    private String nonce;
}
