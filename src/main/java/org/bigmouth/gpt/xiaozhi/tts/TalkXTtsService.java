package org.bigmouth.gpt.xiaozhi.tts;

import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.xiaozhi.forest.TalkXApi;
import org.bigmouth.gpt.xiaozhi.forest.TtsRequest;

import java.util.function.Consumer;

/**
 * @author Allen Hu
 * @date 2025/3/18
 */
@Slf4j
public class TalkXTtsService implements TtsService {

    private final TalkXApi talkXApi;

    public TalkXTtsService(TalkXApi talkXApi) {
        this.talkXApi = talkXApi;
    }

    @Override
    public TtsPlatformType of() {
        return TtsPlatformType.TalkX;
    }

    @Override
    public void stream(String text, Consumer<byte[]> frameHandler) {
        TtsRequest request = new TtsRequest();
        request.setText(text);
        byte[] bytes = talkXApi.tts(request);
        frameHandler.accept(bytes);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
