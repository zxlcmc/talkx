package org.bigmouth.gpt.ai;

import org.bigmouth.gpt.ai.entity.voice.Speech2TextArgument;

/**
 * @author huxiao
 * @date 2023/11/13
 * @since 1.0.0
 */
public interface Speech2TextService {

    String transcriptions(Speech2TextArgument argument);
}
