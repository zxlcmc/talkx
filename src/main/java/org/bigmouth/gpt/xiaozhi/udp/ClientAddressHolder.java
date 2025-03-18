package org.bigmouth.gpt.xiaozhi.udp;

import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.utils.KeyBuilder;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Allen Hu
 * @date 2025/3/12
 */
@Configuration
@AllArgsConstructor
public class ClientAddressHolder {

    private final Fetcher fetcher;
    private final Updater updater;
    private final int expireTimeInSeconds = (int) Duration.ofMinutes(30).getSeconds();

    public void save(String sessionId, ClientAddress clientAddress) {
        updater.update(stringClientAddressKeyGenerator(sessionId), clientAddress, expireTimeInSeconds);
    }

    public ClientAddress get(String sessionId) {
        return fetcher.fetch(stringClientAddressKeyGenerator(sessionId), ClientAddress.class);
    }

    public void remove(String sessionId) {
        updater.remove(stringClientAddressKeyGenerator(sessionId));
    }

    public void refresh(String sessionId) {
        updater.expire(stringClientAddressKeyGenerator(sessionId), expireTimeInSeconds);
    }

    private KeyGenerator stringClientAddressKeyGenerator(String sessionId) {
        return () -> KeyBuilder.build("talkx", "udp", "client_address", sessionId);
    }
}
