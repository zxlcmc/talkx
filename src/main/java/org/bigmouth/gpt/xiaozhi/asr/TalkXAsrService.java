package org.bigmouth.gpt.xiaozhi.asr;

import org.bigmouth.gpt.xiaozhi.entity.asr.TalkXAsrRequest;
import org.bigmouth.gpt.xiaozhi.forest.TalkXApi;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Configuration
public class TalkXAsrService implements AsrService {

    private final TalkXApi talkXApi;

    public TalkXAsrService(TalkXApi talkXApi) {
        this.talkXApi = talkXApi;
    }

    @Override
    public AsrType of() {
        return AsrType.TALKX;
    }

    @Override
    public String asr(String key, byte[] bytes) throws Exception {
        return talkXApi.asr(new TalkXAsrRequest(key, bytes));
    }
}
