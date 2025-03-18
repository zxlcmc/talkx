package org.bigmouth.gpt.xiaozhi.asr;

import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.xiaozhi.entity.asr.SenseVoiceFastApiResult;
import org.bigmouth.gpt.xiaozhi.forest.SenseVoiceFastApi;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/1/10
 */
@Slf4j
@Configuration
public class SenseVoiceFastApiService implements AsrService {

    private final SenseVoiceFastApi senseVoiceFastApi;

    public SenseVoiceFastApiService(SenseVoiceFastApi senseVoiceFastApi) {
        this.senseVoiceFastApi = senseVoiceFastApi;
    }

    @Override
    public AsrType of() {
        return AsrType.SENSEVOICE_FASTAPI;
    }

    @Override
    public String asr(String key, byte[] bytes) throws Exception {
        SenseVoiceFastApiResult result = senseVoiceFastApi.asr(bytes, key, "auto");
        return result.getResult().get(0).getText();
    }
}
