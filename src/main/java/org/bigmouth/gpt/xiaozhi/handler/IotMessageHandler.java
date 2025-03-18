package org.bigmouth.gpt.xiaozhi.handler;

import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.utils.KeyBuilder;
import org.bigmouth.gpt.xiaozhi.entity.DataPacket;
import org.bigmouth.gpt.xiaozhi.entity.MessageType;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author Allen Hu
 * @date 2025/2/21
 */
@Configuration
public class IotMessageHandler implements MessageHandler {

    public static final String FIELD_DESCRIPTIONS = "descriptions";
    public static final String FIELD_STATES = "states";
    private final Fetcher fetcher;
    private final Updater updater;

    public IotMessageHandler(Fetcher fetcher, Updater updater) {
        this.fetcher = fetcher;
        this.updater = updater;
    }

    @Override
    public MessageType onType() {
        return MessageType.IOT;
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        String sessionId = dataPacket.getSessionId();
        List<DataPacket.Descriptor> descriptors = dataPacket.getDescriptors();
        if (descriptors != null && !descriptors.isEmpty()) {
            updater.hupdate(hashIot(sessionId), FIELD_DESCRIPTIONS, dataPacket, 30 * 60);
        }
        List<DataPacket.State> states = dataPacket.getStates();
        if (states != null && !states.isEmpty()) {
            updater.hupdate(hashIot(sessionId), FIELD_STATES, dataPacket, 30 * 60);
        }
        return null;
    }

    public Map<String, DataPacket> get(String sessionId) {
        return fetcher.hfetchall(hashIot(sessionId), DataPacket.class);
    }

    public void refresh(String sessionId) {
        updater.expire(hashIot(sessionId), 30 * 60);
    }

    private static KeyGenerator hashIot(String sessionId) {
        return () -> KeyBuilder.build("talkx", "iot", "data", sessionId);
    }
}
