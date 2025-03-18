package org.bigmouth.gpt.xiaozhi.tts;

import org.bigmouth.gpt.xiaozhi.config.XiaozhiAlibabaConfig;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiByteDanceConfig;
import org.bigmouth.gpt.xiaozhi.forest.TalkXApi;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * @author Allen Hu
 * @date 2025/2/27
 */
@Configuration
public class TtsServiceFactory {

    private final XiaozhiAlibabaConfig xiaozhiAlibabaConfig;
    private final XiaozhiByteDanceConfig xiaozhiByteDanceConfig;
    private final TalkXApi talkXApi;

    public TtsServiceFactory(XiaozhiAlibabaConfig xiaozhiAlibabaConfig, XiaozhiByteDanceConfig xiaozhiByteDanceConfig, TalkXApi talkXApi) {
        this.xiaozhiAlibabaConfig = xiaozhiAlibabaConfig;
        this.xiaozhiByteDanceConfig = xiaozhiByteDanceConfig;
        this.talkXApi = talkXApi;
    }

    public TtsService createInstance(TtsPlatformType ttsPlatformType, String voiceModel, String voiceRole) {
        switch (ttsPlatformType) {
            case Alibaba:
                String dashscopeApiKey = xiaozhiAlibabaConfig.getDashscopeApiKey();
                voiceModel = Optional.ofNullable(voiceModel).orElse(xiaozhiAlibabaConfig.getCosyVoiceDefaultModel());
                voiceRole = Optional.ofNullable(voiceRole).orElse(xiaozhiAlibabaConfig.getCosyVoiceDefaultVoice());
                return new AlibabaDashscopeTtsService(dashscopeApiKey, voiceModel, voiceRole);
            case ByteDance:
                String ttsUrl = xiaozhiByteDanceConfig.getTtsUrl();
                String appId = xiaozhiByteDanceConfig.getAppId();
                String accessToken = xiaozhiByteDanceConfig.getAccessToken();
                voiceRole = Optional.ofNullable(voiceRole).orElse(xiaozhiByteDanceConfig.getDefaultVoice());
                return new ByteDanceTtsService(ttsUrl, appId, accessToken, voiceRole);
            case TalkX:
                return new TalkXTtsService(talkXApi);
            default:
                throw new IllegalArgumentException("Unsupported TTS platform type: " + ttsPlatformType);
        }
    }
}
