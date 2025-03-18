package org.bigmouth.gpt.ai;

import org.bigmouth.gpt.ai.entity.voice.Text2SpeechArgument;

/**
 * @author huxiao
 * @date 2023/11/10
 * @since 1.0.0
 */
public interface Text2SpeechService {

    byte[] speech(Text2SpeechArgument argument);
}
