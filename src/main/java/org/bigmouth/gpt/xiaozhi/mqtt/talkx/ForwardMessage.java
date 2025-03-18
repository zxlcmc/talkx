package org.bigmouth.gpt.xiaozhi.mqtt.talkx;

import lombok.Data;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Data
public class ForwardMessage {

    public static final String ACTION_SEND_MSG = "send_msg";

    private String id;
    private String action = ACTION_SEND_MSG;
    private String topic;
    private String json;
}
