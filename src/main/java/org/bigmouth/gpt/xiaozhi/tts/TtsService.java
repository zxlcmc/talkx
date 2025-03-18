package org.bigmouth.gpt.xiaozhi.tts;

import java.util.function.Consumer;

public interface TtsService {

    TtsPlatformType of();

    void stream(String text, Consumer<byte[]> frameHandler);

    void init();

    void destroy();
}
