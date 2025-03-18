package org.bigmouth.gpt.xiaozhi.tts;

import cn.hutool.core.io.FileUtil;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import com.alibaba.dashscope.common.ResultCallback;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.bigmouth.gpt.xiaozhi.audio.PcmToWavUtil;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author Allen Hu
 * @date 2025/2/24
 */
@Slf4j
@Getter
public class AlibabaDashscopeTtsService implements TtsService {

    private final String apiKey;
    private final String model;
    private final String voice;

    private SpeechSynthesisParam param;

    private SpeechSynthesizer speechSynthesizer;

    public AlibabaDashscopeTtsService(String apiKey, String model, String voice) {
        this.apiKey = apiKey;
        this.model = model;
        this.voice = voice;
        this.param = SpeechSynthesisParam.builder()
                .apiKey(apiKey)
                .model(model)
                .voice(voice)
                .format(SpeechSynthesisAudioFormat.PCM_16000HZ_MONO_16BIT)
                .build();
    }

    @Override
    public TtsPlatformType of() {
        return TtsPlatformType.Alibaba;
    }

    @Override
    public void init() {
        this.speechSynthesizer = new SpeechSynthesizer(param, null);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void stream(String text, Consumer<byte[]> frameHandler) {
        ResultCallback<SpeechSynthesisResult> callback = new ResultCallback<SpeechSynthesisResult>() {
            @Override
            public void onEvent(SpeechSynthesisResult speechSynthesisResult) {
                ByteBuffer audioFrame = speechSynthesisResult.getAudioFrame();
                if (null != audioFrame) {
                    if (null != frameHandler) {
                        frameHandler.accept(audioFrame.array());
                    }
                }
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void onError(Exception e) {
                log.error("onError: {}", e.getMessage());
            }
        };
        speechSynthesizer.updateParamAndCallback(param, callback);
        speechSynthesizer.streamingCall(text);
        speechSynthesizer.streamingComplete();
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        AlibabaDashscopeTtsService ttsService = new AlibabaDashscopeTtsService(apiKey,
                "cosyvoice-v1",
                "longjielidou");
        ttsService.init();
        for (int i = 0; i < 1; i++) {
            AtomicBoolean isFirst = new AtomicBoolean(true);
            List<byte[]> pcmBytes = Lists.newArrayList();
            String text = "闭上眼睛，听听内心的声音，感受这份宁静美好。";
            ttsService.stream(text, bytes -> {
                System.out.println(isFirst.compareAndSet(true, false) + " - " + text + " - " + Hex.encodeHexString(bytes));
                pcmBytes.add(bytes);
            });
            byte[] wav = PcmToWavUtil.pcmToWav(UdpClientContext.mergeByteArrays(pcmBytes));
            String path = System.getProperty("user.home") + java.io.File.separator + "Downloads" + File.separator + "test-" + RandomStringUtils.randomNumeric(8) + ".wav";
            FileUtil.writeBytes(wav, path);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ttsService.destroy();
    }
}
