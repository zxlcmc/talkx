package org.bigmouth.gpt.xiaozhi.tts;

import com.bxm.warcar.utils.JsonHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiTalkXConfig;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Allen Hu
 * @date 2025/3/18
 */
@Slf4j
public class TalkXTtsService implements TtsService {

    private final String talkxCenterBaseUrl;
    private final OkHttpClient okHttpClient = create();
    private final XiaozhiTalkXConfig xiaozhiTalkXConfig;
    private final TalkXTtsService.TtsConfig ttsConfig;

    public TalkXTtsService(String talkxCenterBaseUrl, XiaozhiTalkXConfig xiaozhiTalkXConfig, TtsConfig ttsConfig) {
        this.talkxCenterBaseUrl = talkxCenterBaseUrl;
        this.xiaozhiTalkXConfig = xiaozhiTalkXConfig;
        this.ttsConfig = ttsConfig;
    }

    private OkHttpClient create() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(100, 10, TimeUnit.MINUTES))
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofMinutes(10))
                .build();
    }

    @Override
    public TtsPlatformType of() {
        return TtsPlatformType.TalkX;
    }

    @Override
    public void stream(String text, Consumer<byte[]> frameHandler) {
        Response response = null;
        try {
            ttsConfig.setText(text);

            Request request = new Request.Builder()
                    .url(talkxCenterBaseUrl)
                    .post(RequestBody.create(JsonHelper.convert(ttsConfig), MediaType.get("application/json")))
                    .build();

            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                InputStream inputStream = response.body().byteStream();
                byte[] buffer = new byte[xiaozhiTalkXConfig.getTtsStreamBufferSize()];
                while (inputStream.read(buffer) != -1) {
                    frameHandler.accept(buffer);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred while processing the request: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Data
    public static class TtsConfig {
        /**
         * sessionId
         */
        private String sessionId;
        /**
         * 平台
         */
        private TtsPlatformType ttsPlatformType;
        /**
         * 合成大模型
         */
        private String voiceModel;
        /**
         * 音色角色
         */
        private String voiceRole;

        /**
         * 说话内容
         */
        private String text;
    }
}
