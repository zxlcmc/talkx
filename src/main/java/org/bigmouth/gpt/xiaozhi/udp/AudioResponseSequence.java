package org.bigmouth.gpt.xiaozhi.udp;

import com.bxm.warcar.cache.Counter;
import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.utils.KeyBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Optional;

/**
 * @author Allen Hu
 * @date 2025/3/12
 */
@Slf4j
public class AudioResponseSequence {

    private final Counter counter;
    private final String sessionId;

    public AudioResponseSequence(Counter counter, String sessionId) {
        this.counter = counter;
        this.sessionId = sessionId;
    }

    public int get() {
        Long l = Optional.ofNullable(counter.get(stringAudioResponseSequenceKeyGenerator(sessionId))).orElse(1L);
        return l.intValue();
    }

    /**
     * 增加并获取
     * @return
     */
    public int incrementAndGet() {
        return counter.incrementAndGet(stringAudioResponseSequenceKeyGenerator(sessionId), (int) Duration.ofDays(3).getSeconds()).intValue();
    }

    /**
     * 续租
     */
    public void renewLease() {
        counter.expire(stringAudioResponseSequenceKeyGenerator(sessionId), (int) Duration.ofDays(3).getSeconds());
    }

    private static KeyGenerator stringAudioResponseSequenceKeyGenerator(String sessionId) {
        return () -> KeyBuilder.build("talkx", "audio", "seq", sessionId);
    }
}
