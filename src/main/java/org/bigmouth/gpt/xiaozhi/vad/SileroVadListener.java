package org.bigmouth.gpt.xiaozhi.vad;

import ai.onnxruntime.OrtException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Allen Hu
 * @date 2025/2/22
 */
@Slf4j
public class SileroVadListener {

    public static final String MODEL_PATH = "src/main/conf/silero_vad.onnx";
    public static final int SAMPLE_RATE = 16000;
    public static final float START_THRESHOLD = 0.6f;
    public static final float END_THRESHOLD = 0.45f;
    public static final int MIN_SILENCE_DURATION_MS = 2000;
    public static final int SPEECH_PAD_MS = 60;

    private final String modelPath;
    private final int sampleRate;
    private final float startThreshold;
    private final float endThreshold;
    private final int minSilenceDurationMs;
    private final int speechPadMs;

    private SileroVadDetector vadDetector;
    private volatile AtomicBoolean stop = new AtomicBoolean(false);

    public SileroVadListener(String modelPath, int sampleRate, float startThreshold, float endThreshold, int minSilenceDurationMs, int speechPadMs) {
        this.modelPath = modelPath;
        this.sampleRate = sampleRate;
        this.startThreshold = startThreshold;
        this.endThreshold = endThreshold;
        this.minSilenceDurationMs = minSilenceDurationMs;
        this.speechPadMs = speechPadMs;
    }

    public void init() {
        try {
            this.vadDetector = new SileroVadDetector(modelPath, startThreshold, endThreshold, sampleRate, minSilenceDurationMs, speechPadMs);
            this.stop.set(false);
        } catch (OrtException e) {
            log.error("Error initializing the VAD detector: ", e);
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        try {
            if (stop.compareAndSet(false, true)) {
                if (null != vadDetector) {
                    this.vadDetector.close();
                    this.vadDetector = null;
                }
            }
        } catch (OrtException e) {
            log.error("Error closing the VAD detector: ", e);
        }
    }

    public Map<String, Double> listen(byte[] data) {
        try {
            if (null == vadDetector) {
                return null;
            }
            return vadDetector.apply(data, true);
        } catch (IllegalArgumentException ignored) {
        } catch (Exception e) {
            log.error("Error applying VAD detector: ", e);
        }
        return null;
    }

    public boolean isStop() {
        return stop.get();
    }
}
