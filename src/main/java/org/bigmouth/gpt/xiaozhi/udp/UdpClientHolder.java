package org.bigmouth.gpt.xiaozhi.udp;

import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.utils.KeyBuilder;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Configuration
public class UdpClientHolder {

    private final Updater updater;
    private final Fetcher fetcher;

    public UdpClientHolder(Updater updater, Fetcher fetcher) {
        this.updater = updater;
        this.fetcher = fetcher;
    }

    public void saveClient(String sessionId, InetAddress clientAddress, int clientPort) {
        updater.update(stringClientAddress(sessionId), clientAddress.getHostName() + ":" + clientPort);
    }

    public ClientAddress getClient(String sessionId) {
        String clientInf = fetcher.fetch(stringClientAddress(sessionId), String.class);
        String[] ipport = clientInf.split(":");
        return new ClientAddress()
                .setIp(ipport[0])
                .setPort(Integer.parseInt(ipport[1]));
    }

    private KeyGenerator stringClientAddress(String sessionId) {
        return () -> KeyBuilder.build("talkx", "udp", "client", sessionId);
    }
}
