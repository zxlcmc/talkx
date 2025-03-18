package org.bigmouth.gpt.xiaozhi.audio;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Data
@Accessors(chain = true)
public class AudioResponsePacket {

    private int pcmSize;
    private byte[] encrypted;
}
