package org.bigmouth.gpt.xiaozhi.event;

import lombok.Getter;
import org.bigmouth.gpt.xiaozhi.entity.UdpHello;

import java.util.EventObject;

/**
 * 对话完成事件，包括TTS语音数据发送完毕。
 * @author Allen Hu
 * @date 2025/2/26
 */
@Getter
public class ChatCompletionEndEvent extends EventObject {

    private final UdpHello udpHello;

    public ChatCompletionEndEvent(Object source, UdpHello udpHello) {
        super(source);
        this.udpHello = udpHello;
    }
}
